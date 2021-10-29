package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make

/**
 * Responsible for finding and asserting paths inside the project.
 */
open class DirectoryResolver(open val defaultFolder: String) {
    protected val defaultFolderWithoutTrailingSlash: String
        get() = defaultFolder.subSequence(0, defaultFolder.length - 2).toString()

    /**
     * Determines, if the passed path could point to our default directory.
     *
     * This is used, so in the context menu for a new file, only certain actions are
     * activated. For example if you right-click on "/app/Http", we only activate
     * the "new Controller" and "new Middleware" actions, as those files are stored
     * somewhere in that directory.
     */
    fun couldPointToDefaultFolder(relativePathFromProjectRoot: String): Boolean {
        // Somebody clicks on "/app/Http/Controllers", then this is true for the "new Controller" action
        // BUT, when I click on "/app/Http/Controllers/MyNamespace" the above is false, so we need to
        // also check, if we are **below** our default directory.
        return isAboveDefaultDirectory(relativePathFromProjectRoot) ||
            isBelowDefaultDirectory(relativePathFromProjectRoot)
    }

    open fun isAboveDefaultDirectory(relativePathFromProjectRoot: String): Boolean {
        return defaultFolderWithoutTrailingSlash.startsWith(relativePathFromProjectRoot)
    }

    open fun isBelowDefaultDirectory(relativePathFromProjectRoot: String): Boolean {
        return relativePathFromProjectRoot.startsWith(defaultFolderWithoutTrailingSlash)
    }
}
