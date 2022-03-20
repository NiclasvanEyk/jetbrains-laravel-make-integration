package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAwareAction

class AutoconfigureLaravelSailAction(private val hideIcon: Boolean = false): DumbAwareAction() {
    init {
        templatePresentation.apply {
            icon = if (hideIcon) null else LaravelIcons.Sail
            text = "Autoconfigure Laravel Sail"
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
        val log = logger<AutoconfigureLaravelSailAction>()
        val application = e.project?.laravel() ?: return // TODO: Notification?

        SailConfigurationProvider.EP_NAME.extensionList.forEach {
            try {
                log.debug("Executing '${it.javaClass.name}'...")
                it.apply(application)
            } catch (exception: Throwable) {
                logger<AutoconfigureLaravelSailAction>().error(exception)
            }
        }

        SailNotificationGroup
            .info(
                title = "Laravel Sail Setup Completed",
                content = """
                    The IDE will now use Laravel Sail for running PHP, Node 
                    and NPM related tasks! 
                """.trimIndent()
                // TODO: At some point I'd like to add two example actions here, so the
                //       user can click on "start containers", "execute tests" or
                //       "compile frontend assets" and thereby can setup those run
                //       configurations in an easy way
                // Try running one of the options below to get started.
            )
            .notify(application.project)
    }
}