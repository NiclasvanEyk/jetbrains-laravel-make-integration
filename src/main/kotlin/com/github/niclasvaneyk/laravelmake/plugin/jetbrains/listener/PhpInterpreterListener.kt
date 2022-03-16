package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.listener

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.php.config.PhpProjectWorkspaceConfigurationListener

/**
 * Gets triggered when the PHP interpreter is changed.
 *
 * Then ensures it is valid and re-initializes the [LaravelApplication] if
 * present.
 */
class PhpInterpreterListener(private val project: Project): PhpProjectWorkspaceConfigurationListener {
    override fun interpreterChanged() {
        val laravel = project.service<LaravelMakeProjectService>().application ?: return

        if (!laravel.wasInitialized) {
            laravel.initialize()
        } else {
            laravel.introspection.refresh()
        }
    }
}