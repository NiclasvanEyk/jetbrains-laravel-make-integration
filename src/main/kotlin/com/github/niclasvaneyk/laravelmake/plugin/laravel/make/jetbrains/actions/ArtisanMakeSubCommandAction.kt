package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.DirectoryResolver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains.ArtisanMakeSubCommandActionExecution
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.SubCommand
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile

/**
 * Class for handling the event from the IDE.
 *
 * This mainly validates the prerequisites for executing artisan.
 */
abstract class ArtisanMakeSubCommandAction(
    val command: SubCommand,
    isFromContextMenu: Boolean = false
) : DumbAwareAction(LaravelIcons.LaravelLogo) {
    var isFromContextMenu: Boolean = false
        set(value) {
            templatePresentation.text =
                // If this Action is displayed in the context menu, we want to
                // display the short name, as the context is clear
                if (value) command.capitalized
                // Otherwise, the action is displayed out of context, e.g while
                // searching in the double-shift menu. Then we want to provide as
                // much information as possible, so we show the whole description
                // as the text.
                else command.description
            field = value
        }

    init {
        this.isFromContextMenu = isFromContextMenu
        templatePresentation.description = command.description
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val service = project.getService(LaravelMakeProjectService::class.java)

        if (!service.isLaravelProject) {
            return
        }

        val targetFilePath = targetFileFromEvent(event)?.canonicalPath
        val laravelProject = service.application!!

        val execution = buildExecution(
            command, project, laravelProject, targetFilePath
        )

        execution.execute()
    }

    open fun buildExecution(
        meta: SubCommand,
        project: Project,
        laravelApplication: LaravelApplication,
        targetFilePath: String?
    ): ArtisanMakeSubCommandActionExecution {
        return ArtisanMakeSubCommandActionExecution(
            meta,
            project,
            laravelApplication,
            targetFilePath
        )
    }

    override fun update(event: AnActionEvent) {
        super.update(event)

        event.presentation.isEnabledAndVisible = true
        event.presentation.description = command.description

        if (!isFromContextMenu) return

        // We do not want to filter anything from the double shift thingy
        if (event.isFromActionToolbar) return

        val app = event.project
            ?.service<LaravelMakeProjectService>()
            ?.application ?: return

        val targetFilePath = targetFileFromEvent(event)?.canonicalPath ?: return
        val relativeTargetPath = app.paths.fromProjectRoot(targetFilePath)

        event.presentation.isEnabled = shouldBeActivatedWhenRightClickedOn(relativeTargetPath)
        event.presentation.description = command.description
            .plus(" (disabled, as this is not the directory where artisan:make would create files of this type)")
    }

    open fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return DirectoryResolver(command.location).couldPointToDefaultFolder(relativePathFromProjectRoot)
    }

    private fun targetFileFromEvent(event: AnActionEvent): VirtualFile? {
        val view = LangDataKeys.IDE_VIEW.getData(event.dataContext) ?: return null
        val directories = view.directories

        return if (directories.isNotEmpty()) directories[0].virtualFile else null
    }
}
