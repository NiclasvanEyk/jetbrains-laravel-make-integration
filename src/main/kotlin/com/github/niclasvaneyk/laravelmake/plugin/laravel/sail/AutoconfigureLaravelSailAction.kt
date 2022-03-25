package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem.LaravelAction
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings.settings
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.DockerSetupForSailAutoconfiguration
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbAwareAction

class AutoconfigureLaravelSailAction(private val hideIcon: Boolean = false): LaravelAction(), DumbAware {
    companion object {
        fun notification() = SailNotificationGroup.info(
            title = "Laravel Sail Setup",
            content = """
                    It seems like this project uses the default Laravel Sail setup.
                    To get a better development experience, the IDE can be 
                    automatically configured to use the PHP and NPM installations from
                    the Sail container.
                """.trimIndent()
        ).apply {
            isImportant = true
            addAction(AutoconfigureLaravelSailAction(hideIcon = true))
            addAction(object: DumbAwareAction(LaravelMakeIntegrationBundle.message("notification.dont-ask-again")) {
                override fun actionPerformed(e: AnActionEvent) {
                    e.project?.laravel()?.settings?.shouldDisplaySailAutoconfigurationPopup = false
                    this@apply.expire()
                }
            })
        }
    }

    init {
        templatePresentation.apply {
            icon = if (hideIcon) null else LaravelIcons.Sail
            text = "Autoconfigure Laravel Sail"
            description = "Configures your IDE to use Laravel Sail for PHP, Node and Docker Compose."
            isVisible = false // Will only be enabled for Laravel applications
            isEnabled = true
        }
    }

    override fun run(application: LaravelApplication) {
        val log = logger<AutoconfigureLaravelSailAction>()
        val dockerSetup = application.project.service<DockerSetupForSailAutoconfiguration>()

        if (!dockerSetup.hasBeenCompleted) {
            dockerSetup.begin()
            return
        }

        SailConfigurationProvider.EP_NAME.extensionList.forEach {
            try {
                log.debug("Executing '${it.javaClass.name}'...")
                it.apply(application)
            } catch (exception: Throwable) {
                logger<AutoconfigureLaravelSailAction>().error(exception)
            }
        }

        application.initialize()

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