package laravel_make_integration.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import laravel_make_integration.resolveLaravelProject
import laravel_make_integration.targetFileFromEvent
import laravel_make_integration.tryToOpenFile

/**
 * Can be used if the type of class to be generated has a default location.
 *
 * Example: Controllers are living under `app/Http/Controllers`. If a User runs this action inside
 * a subdirectory of this folder, we can pre-fill the input field for the classname with the folder
 * path. This way artisan generates the class inside the directory that this action was executed from
 * and also uses the correct namespace.
 */
abstract class NamespacedArtisanMakeAction: ArtisanMakeAction() {
    var doCheckForSubfolders: Boolean = false

    override fun actionPerformed(event: AnActionEvent) {
        preActionPerformed(event) ?: return

        val name = askForClassName(event.project!!, targetFilePath)

        if (name.isNullOrEmpty()) return

        val success = laravelProject.artisan.make(subCommand(), listOf(name))

        if (!success) return

        val createdFilePath = projectBasePath + defaultFolder() + "$name.php"

        tryToOpenFile(event.project!!, createdFilePath)
    }

    /**
     * Computes the namespace for the controller based on the location in the View-Tab.
     *
     * If we right click inside a subdirectory of the app/Http/Controllers-Folder, we can prepend
     * the path to this subdirectory as a namespace to the name of the controller.
     *
     * The artisan make:controller command will then create the controller inside the subdirectory.
     */
    override fun getInitialInputValue(target: String): String {
        val relativeTarget = target.replace(projectBasePath, "")
        val subFolder = "$relativeTarget/".replace(defaultFolder(), "")

        // We only want to add the namespace, when we are inside the default folder
        if (!isBelowDefaultDirectory(relativeTarget)) return super.getInitialInputValue(target)

        return if (subFolder.isNotBlank()) subFolder else ""
    }

    /**
     * The default folder for the classes to be created in.
     *
     * Should begin and end with a /
     */
    abstract fun defaultFolder(): String

    private val defaultFolderWithoutTrailingSlash: String
        get() = defaultFolder().subSequence(0, defaultFolder().length - 2).toString()

    override fun update(event: AnActionEvent) {
        super.update(event)

        println("activating action for ${subCommand()}")
        event.presentation.isEnabledAndVisible = true

        if (!doCheckForSubfolders) return

        // We do not want to filter anything from the double shift thingy
        if (event.isFromActionToolbar) return

        val project = resolveLaravelProject(event) ?: return

        val targetFilePath = targetFileFromEvent(event)?.canonicalPath ?: return
        val relativeTargetPath = project.paths.fromProjectRoot(targetFilePath)

        event.presentation.isEnabled = shouldBeActivatedWhenRightClickedOn(relativeTargetPath)

        if (!event.presentation.isEnabled) println("${subCommand()} got disabled!")
    }

    open fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return couldPointToDefaultFolder(relativePathFromProjectRoot)
    }

    /**
     * Determines, if the passed path could point to our default directory.
     *
     * This is used, so in the context menu for a new file, only certain actions are
     * activated. For example if you right-click on "/app/Http", we only activate
     * the "new Controller" and "new Middleware" actions, as those files are stored
     * somewhere in that directory.
     */
    private fun couldPointToDefaultFolder(relativePathFromProjectRoot: String): Boolean {
        // Somebody clicks on "/app/Http/Controllers", then this is true for the "new Controller" action
        // BUT, when I click on "/app/Http/Controllers/MyNamespace" the above is false, so we need to
        // also check, if we are **below** our default directory.
        return isAboveDefaultDirectory(relativePathFromProjectRoot)
                || isBelowDefaultDirectory(relativePathFromProjectRoot)
    }

    private fun isAboveDefaultDirectory(relativePathFromProjectRoot: String): Boolean {
        return defaultFolderWithoutTrailingSlash.startsWith(relativePathFromProjectRoot)
    }

    private fun isBelowDefaultDirectory(relativePathFromProjectRoot: String): Boolean {
        return relativePathFromProjectRoot.startsWith(defaultFolderWithoutTrailingSlash)
    }
}