package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.niclas_van_eyk.laravel_make_integration.ide.jetbrains.runWithProgress
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

    init {
        val commandNames = runWithProgress<List<String>>(Computable {
            val result = laravelProject.artisan.execute(project, "")

            if (result.wasFailure) {
                print(result.log) // TODO
            }

            return@Computable parseArtisanMakeCommandNames(result.log)
        }, "Detecting available commands").get()

        val commands = runWithProgress<MutableList<Command>>(Computable {
            val commands = mutableListOf<Command>()

            for (commandName in commandNames) {
                val parts = commandName.split(":")
                val result = laravelProject.artisan.execute(project, parts[0], parts[1], listOf("--help"))

                if (result.wasFailure) {
                    continue
                }

                commands.add(parseArtisanCommandFromHelp(result.logWithoutNewLines.trim(), commandName))
            }

            return@Computable commands
        }).get()

        WellKnownCommandInformation().updateOptionTypes(commands)

        this.commands = commands
    }
}