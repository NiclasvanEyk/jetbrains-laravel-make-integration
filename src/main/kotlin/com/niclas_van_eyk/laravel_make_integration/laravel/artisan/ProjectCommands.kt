package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

import com.intellij.concurrency.SensitiveProgressWrapper
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.services.LaravelMakeIntegrationProjectService

/**
 * This class runs [parseArtisanMakeCommandNames] and [parseArtisanCommandFromHelp],
 * to get a structured list of all make:* commands and their parameters/options/flags,
 * etc.
 *
 * This most likely takes a few seconds, as we execute `php artisan make:command --help`
 * several times and connecting to the php interpreter can take some time (e.g.) if it
 * is inside a docker container that has to be started first). The use of this class
 * is mainly to schedule these operations and display a nice progress bar at the bottom
 * of the ide in the meantime.
 *
 * @see LaravelMakeIntegrationProjectService.commands
 */
class ProjectCommands(val laravelProject: LaravelProject, val project: Project) {
    var commands = mutableListOf<Command>()

    fun inferFromHelp() {
        val progressManager = ProgressManager.getInstance()

        progressManager.run(object: Backgroundable(project, "Scanning for Artisan commands", true, ALWAYS_BACKGROUND) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                val commandScanResult = laravelProject.artisan.execute(project, "")

                if (commandScanResult.wasFailure) {
                    print(commandScanResult.log) // TODO
                    indicator.stop()
                    return
                }

                val commandNames = parseArtisanMakeCommandNames(commandScanResult.log)

                indicator.isIndeterminate = false
                indicator.fraction = 0.0

                for ((i, commandName) in commandNames.withIndex()) {
                    val parts = commandName.split(":")
                    val result = laravelProject.artisan.execute(project, parts[0], parts[1], listOf("--help"))

                    if (result.wasFailure) {
                        continue
                    }

                    this@ProjectCommands.commands.add(parseArtisanCommandFromHelp(result.logWithoutNewLines.trim(), commandName))
                    indicator.fraction = (i + 1).toDouble() / commandNames.size.toDouble()

                    if (indicator.isCanceled) {
                        return
                    }
                }

                WellKnownCommandInformation().updateOptionTypes(commands)
            }
        })
    }
}