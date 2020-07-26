package com.niclas_van_eyk.laravel_make_integration.actions

import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.util.ProgressWindow
import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.filesystem.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.ide.IdeAdapter
import com.niclas_van_eyk.laravel_make_integration.ide.jetbrains.JetbrainsAdapter
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.run.PHPScriptRun
import com.niclas_van_eyk.laravel_make_integration.run.ScriptRunFailedException

/**
 * With this class we know, that we have a valid Laravel project, so the action
 * that we want to execute will succeed, given that the user provides valid
 * inputs.
 */
open class ArtisanMakeSubCommandActionExecution(
    protected open val command: SubCommand,
    protected open val project: Project,
    protected open val laravelProject: LaravelProject,
    protected open val target: String?
) {
    protected open val directoryResolver: DirectoryResolver
        get() = DirectoryResolver(command.location)

    protected open val targetResolver: TargetResolver
        get() = TargetResolver(directoryResolver)

    protected open val createdFileResolver: CreatedFileResolver
        get() = CreatedFileResolver(laravelProject)

    protected open val ideAdapter: IdeAdapter
        get() = JetbrainsAdapter(project, command)

    fun execute() {
        val initialInput = targetResolver.suggestInitialInputFor(
            target,
            laravelProject.paths.base
        )
        // We don't need to provide any feedback here, since the user either
        // hit cancel or provided no input
        val input = ideAdapter.getUserInput(initialInput) ?: return
        val parameters = ArtisanMakeParameters.fromInput(input)

        try {
            val commandLine = parameters.humanReadable(command.command)

            var makeResult: PHPScriptRun.Result? = null
            val cancelled = !ProgressManager
                .getInstance()
                .runProcessWithProgressSynchronously({
                    val indicator = ProgressIndicatorProvider.getGlobalProgressIndicator()

                    if (indicator != null) {
                        indicator.text = "Attaching to PHP interpreter..."
                    }

                    makeResult = laravelProject.artisan.make(command.command, parameters, project)
                }, commandLine, true, project)

            if (cancelled || makeResult == null) return

            if (makeResult is PHPScriptRun.Result.Failure) {
                ideAdapter.notification((makeResult as PHPScriptRun.Result.Failure).log)
            }

            val createdFilePath = createdFileResolver.getCreatedFilePath(command, parameters)

            if (createdFilePath == null) {
                ideAdapter.notification(
                    "The artisan:make run succeeded, but we were unable to locate the newly created file!"
                )
                return
            }

            println("Success! Trying to open '$createdFilePath'...")

            ideAdapter.openFile(createdFilePath)
        } catch (e: ScriptRunFailedException) {
            var message = "No PHP interpreter found!"
            message += "\nPlease set one in Settings > Languages & Frameworks > PHP"

            ideAdapter.notification(message)
            return
        }
    }
}