package com.niclas_van_eyk.laravel_make_integration.actions

import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver

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
    open fun suggestInitialInputFor(target: String?, projectBasePath: String): String {
        if (target === null) return ""

        val normalizedTarget =
                if (target.endsWith("/")) target.removeSuffix("/")
                else target
        val relativeTarget = normalizedTarget.replace(projectBasePath, "")
        val subFolder = "$relativeTarget/".replace(directoryResolver.defaultFolder, "")

        // We only want to add the namespace, when we are inside the default folder
        if (!directoryResolver.isBelowDefaultDirectory(relativeTarget))
            return ""

        return if (subFolder.isNotBlank()) subFolder.substring(1) else ""
    }
}