package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import java.io.File

/**
 * Determines whether it is likely that the project uses Laravel Sail.
 */
fun LaravelApplication.usesSail(): Boolean {
    val composerJson = File(paths.path("composer.json"))
    val composerJsonContainsSail = composerJson.isFile && composerJson
        .readText()
        .contains("\"laravel/sail\"")

    val dockerComposeYml = File(paths.path("docker-compose.yml"))
    val composeContainsLaravelService = dockerComposeYml.isFile && dockerComposeYml
        .readText()
        .contains("laravel.test")

    return composerJsonContainsSail && composeContainsLaravelService
}

/**
 * Determines whether we should call [showConfigureSailNotification] or if
 * everything is set up already.
 *
 * It is expected, that the [LaravelApplication] uses Sail!
 */
fun LaravelApplication.hasUnconfiguredSailComponents(): Boolean {
    val configurationProviders = SailConfigurationProvider.EP_NAME.extensionList

    return configurationProviders.any { !it.configurationExists(this) }
}

/**
 * Facade-like thing for the Sail autoconfiguration.
 */
class SailAutoconfiguration {
    companion object {
        fun shouldBeShownFor(application: LaravelApplication): Boolean {
            return application.usesSail()
                && application.hasUnconfiguredSailComponents()
                && !hasAlreadyBeenPrompted
        }

        val action get() = AutoconfigureLaravelSailAction()

        // Quick and dirty way of not showing the popup twice during one "session"
        private var hasAlreadyBeenPrompted = false

        fun promptIfNecessary(application: LaravelApplication) {
            if (!shouldBeShownFor(application)) return

            hasAlreadyBeenPrompted = true
            AutoconfigureLaravelSailAction.notification().notify(application.project)
        }
    }
}
