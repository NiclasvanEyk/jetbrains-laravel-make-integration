package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * An action that is intended only for projects containing a Laravel
 * application.
 *
 * Otherwise, the action will not be visible and abort if no Laravel
 * application is available at runtime.
 */
abstract class LaravelAction: AnAction(LaravelIcons.LaravelLogo) {
    override fun update(e: AnActionEvent) {
        templatePresentation.isVisible = e.project?.laravel() != null
    }

    abstract fun run(application: LaravelApplication)

    override fun actionPerformed(e: AnActionEvent) {
        val laravel = e.project?.laravel() ?: return

        run(laravel)
    }
}