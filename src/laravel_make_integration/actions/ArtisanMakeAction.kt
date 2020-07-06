package laravel_make_integration.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import laravel_make_integration.LaravelIcons
import laravel_make_integration.filesystem.findArtisanBinaryDirectory
import laravel_make_integration.targetFileFromEvent
import laravel_make_integration.tryToOpenFile
import laravel_make_integration.laravel.Project as LaravelProject

abstract class ArtisanMakeAction : DumbAwareAction(LaravelIcons.LaravelLogo) {
    lateinit var projectBasePath: String
    lateinit var targetFilePath: String
    lateinit var laravelProject: LaravelProject
    val laravelProjectInitialized
        get() = ::laravelProject.isInitialized

    init {
        templatePresentation.text = classNameInputLabel()
        templatePresentation.description = actionDescription()
    }

    /**
     * The subcommand for make to run as artisan make:subCommand.
     */
    abstract fun subCommand(): String

    /**
     * The message of the input modal for the classname input.
     */
    open fun classNameInputModalTitle(): String {
        return "New " + classNameInputLabel()
    }

    /**
     * The title of the input modal for the classname input.
     */
    open fun classNameInputLabel(): String {
        return subCommand().capitalize()
    }

    /**
     * The description of the action.
     */
    open fun actionDescription(): String {
        return "Create a new Laravel ${classNameInputLabel()}"
    }

    /**
     * The initial value of the input modal for the classname input.
     */
    protected open fun getInitialInputValue(target: String): String {
        return "";
    }

    protected fun preActionPerformed(event: AnActionEvent): LaravelProject? {
        val targetFile = targetFileFromEvent(event) ?: return null
        targetFilePath = targetFile.canonicalPath ?: return null
        projectBasePath = event.project?.basePath ?: return null
        val artisanDirectory = findArtisanBinaryDirectory(targetFile, projectBasePath) ?: return null

        laravelProject = LaravelProject(artisanDirectory)

        return laravelProject
    }

    protected fun askForClassName(project: Project, target: String): String? {
        val initialInputValue = getInitialInputValue(target)

        val input = Messages.showInputDialog(
                project,
                classNameInputModalTitle(),
                classNameInputLabel(),
                null,
                initialInputValue,
                null,
                TextRange(initialInputValue.length, initialInputValue.length) // this way the cursor is at the end
        )

        if (input.isNullOrEmpty() || input.trim() == initialInputValue.trim()) return null

        return input
    }
}