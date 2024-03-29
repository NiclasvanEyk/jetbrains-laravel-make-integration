package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.jetbrains.php.run.PhpEditInterpreterExecutionException
import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.ProjectBasedNotifier
import com.github.niclasvaneyk.laravelmake.common.laravel.ArtisanMakeParameters
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.common.php.run.NoInterpreterSetException
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunner
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.notifications.LaravelMakeNotificationGroup
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.CreatedFileResolver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.DirectoryResolver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.SubCommand

/**
 * With this class we know, that we have a valid Laravel project, so the action
 * that we want to execute will succeed, given that the user provides valid
 * inputs.
 */
open class ArtisanMakeSubCommandActionExecution(
    protected open val command: SubCommand,
    protected open val project: Project,
    protected open val laravelApplication: LaravelApplication,
    protected open val target: String?,
) {
    protected open val directoryResolver: DirectoryResolver
        get() = DirectoryResolver(command.location)

    protected open val targetResolver: TargetResolver
        get() = TargetResolver(directoryResolver)

    protected open val createdFileResolver: CreatedFileResolver
        get() = CreatedFileResolver(laravelApplication.paths.base)

    @Suppress("SwallowedException")
    fun execute() {
        val initialInput = targetResolver.suggestInitialInputFor(
            target,
            laravelApplication.paths.base
        )
        // We don't need to provide any feedback here, since the user either
        // hit cancel or provided no input
        val dialog = ArtisanMakeDialog(project, command, initialInput)

        if (!dialog.showAndGet()) return

        val parameters = ArtisanMakeParameters.fromInput(dialog.input)
        parameters.parameters.add("--no-interaction")

        val commandLine = parameters.humanReadable(subCommand = command.command)

        var makeResult: PHPRunner.Result? = null
        val cancelled = !ProgressManager
            .getInstance()
            .runProcessWithProgressSynchronously(
                {
                    val indicator = ProgressIndicatorProvider.getGlobalProgressIndicator()

                    if (indicator != null) {
                        indicator.text = "Attaching to PHP interpreter..."
                    }

                    try {
                        makeResult = laravelApplication.artisan.make(command.command, parameters)
                    } catch (e: NoInterpreterSetException) {
                        var message = "No PHP interpreter found!"
                        message += "\nPlease set one in Settings > Languages & Frameworks > PHP"

                        makeResult = PHPRunner.Result(false, arrayListOf(message))
                    } catch (e: PhpEditInterpreterExecutionException) {
                        var message = "Could not connect to the configured remote interpreter."

                        if (e.message?.contains("Docker") == true) {
                            message += " Do you need to start your Docker daemon?"
                        }

                        makeResult = PHPRunner.Result(false, arrayListOf(message))
                    }
                },
                commandLine, true, project
            )

        if (cancelled || makeResult == null) return

        if (makeResult!!.wasFailure) {
            LaravelMakeNotificationGroup
                .error(makeResult?.log ?: "Unknown error while running '$commandLine'")
                .notify(project)
        }

        val createdFilePath = createdFileResolver.getCreatedFilePath(command, parameters)

        if (createdFilePath == null) {
            LaravelMakeNotificationGroup.warn(
                "The artisan:make run succeeded, but we were unable to locate the newly created file!"
            ).notify(project)
            return
        }

        println("Success! Trying to open '$createdFilePath'...")

        val file = LocalFileSystem.getInstance().refreshAndFindFileByPath(createdFilePath)

        if (file != null) {
            ApplicationManager.getApplication().runReadAction {
                OpenFileDescriptor(project, file).navigate(true)
            }
        }
    }
}
