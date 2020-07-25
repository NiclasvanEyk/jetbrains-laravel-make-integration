package com.niclas_van_eyk.laravel_make_integration.actions

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.filesystem.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.ide.IdeAdapter
import com.niclas_van_eyk.laravel_make_integration.ide.JetbrainsAdapter
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
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
        // We don't need any feedback here, since the user hit cancel or provided
        // no input
        val initialInput = targetResolver.suggestInitialInputFor(
            target,
            laravelProject.paths.base
        )
        val input = ideAdapter.getUserInput(initialInput) ?: return
        val parameters = ArtisanMakeParameters.fromInput(input)

        try {
            laravelProject.artisan.make(command.command, parameters, project)
        } catch (e: ScriptRunFailedException) {
            val group = NotificationGroup("laravel_make_integration_errors", NotificationDisplayType.STICKY_BALLOON, true)
            val notification = group.createNotification(
                    "Error running make:${command.command}\n\n" + e.output,
                    NotificationType.ERROR
            )

            notification.isImportant = true

            notification.notify(project)
            return
        }

        val createdFilePath = createdFileResolver.getCreatedFilePath(command, parameters)

        if (createdFilePath == null) {
            return // TODO: Feedback
        }

        println("Success! Trying to open '$createdFilePath'...")

        ideAdapter.openFile(createdFilePath)
    }
}