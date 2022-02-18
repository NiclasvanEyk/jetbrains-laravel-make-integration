package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains

import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.DirectoryResolver

data class InitialInputSuggestion(
    val name: String = "",
    val parameters: List<String> = emptyList()
) {
    val empty get() = name.isBlank() && parameters.isEmpty()

    val toInputString: String
        get() = if (parameters.isNotEmpty()) {
            name + " " + parameters.joinToString(" ")
        } else {
            name
        }
}

/**
 * Handles the initial input for the modal dialog.
 */
open class TargetResolver(protected open val directoryResolver: DirectoryResolver) {
    /**
     * Computes the namespace for the controller based on the location in the View-Tab.
     *
     * If we right click inside a subdirectory of the app/Http/Controllers-Folder, we can prepend
     * the path to this subdirectory as a namespace to the name of the controller.
     *
     * The artisan make:controller command will then create the controller inside the subdirectory.
     */
    open fun suggestInitialInputFor(target: String?, projectBasePath: String): InitialInputSuggestion {
        if (target === null) return InitialInputSuggestion()

        val normalizedTarget = target.removeSuffix("/")
        val relativeTarget = normalizedTarget.replace(projectBasePath, "")
        val subFolder = "$relativeTarget/".replace(directoryResolver.defaultFolder, "")

        // We only want to add the namespace, when we are inside the default folder
        if (!directoryResolver.isBelowDefaultDirectory(relativeTarget)) {
            return InitialInputSuggestion()
        }

        return if (subFolder.isNotBlank()) {
            InitialInputSuggestion(subFolder.substring(1))
        } else {
            InitialInputSuggestion()
        }
    }
}
