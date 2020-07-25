package com.niclas_van_eyk.laravel_make_integration.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.resolveLaravelProject
import com.niclas_van_eyk.laravel_make_integration.services.MyProjectService
import com.niclas_van_eyk.laravel_make_integration.targetFileFromEvent

/**
 * Class for handling the event from the IDE.
 *
 * This mainly validates the prerequisites for executing artisan.
 */
abstract class ArtisanMakeSubCommandAction(
        private val command: SubCommand,
        var disableBasedOnLocation: Boolean = false
): DumbAwareAction() {
    init {
        templatePresentation.text = command.capitalized
        templatePresentation.description = command.description
    }

    fun abort(message: String) {
        // TODO
    }

    override fun actionPerformed(event: AnActionEvent) {
        // TODO: Provide feedback if we return here!

        val project = event.project ?: return
        val service = project.getService(MyProjectService::class.java)

        if (!service.isLaravelProject) {
            return
        }

        val targetFilePath = targetFileFromEvent(event)?.canonicalPath
        val laravelProject = service.laravelProject!!

        val execution = buildExecution(
            command, project, laravelProject, targetFilePath
        )

        execution.execute()
    }

    open fun buildExecution(
        meta: SubCommand,
        project: Project,
        laravelProject: LaravelProject,
        targetFilePath: String?
    ): ArtisanMakeSubCommandActionExecution {
        return ArtisanMakeSubCommandActionExecution(
            meta,
            project,
            laravelProject,
            targetFilePath
        )
    }

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabledAndVisible = true

        if (!disableBasedOnLocation) return

        // We do not want to filter anything from the double shift thingy
        if (event.isFromActionToolbar) return

        val project = resolveLaravelProject(event) ?: return

        val targetFilePath = targetFileFromEvent(event)?.canonicalPath ?: return
        val relativeTargetPath = project.paths.fromProjectRoot(targetFilePath)

        event.presentation.isEnabled = shouldBeActivatedWhenRightClickedOn(relativeTargetPath)
    }

    open fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return DirectoryResolver(command.location).couldPointToDefaultFolder(relativePathFromProjectRoot)
    }
}