package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger

class AddSailRunConfigurationsAction: AnAction() {
    init {
        templatePresentation.apply {
            icon = LaravelIcons.LaravelLogo // TODO: Use the Sail Icon
            text = "Setup Laravel Sail"
            description = "Configures your IDE to use Laravel Sail for PHP, Node and Docker Compose."
            isVisible = false // Will only be enabled for Laravel applications
            isEnabled = false
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)

        if (e.project?.laravel() != null) {
            templatePresentation.apply {
                isEnabled = true
                isVisible = true
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val application = e.project?.laravel() ?: return // TODO: Notification?

        SailConfigurationProvider.EP_NAME.extensionList.forEach {
            try {
                it.apply(application)
            } catch (exception: Throwable) {
                logger<AddSailRunConfigurationsAction>().error(exception)
            }
        }
    }
}