package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication

/**
 * Determines whether we should calls [showConfigureSailNotification] or if
 * everything is setup already.
 *
 * It is expected, that the [LaravelApplication] uses Sail!
 */
fun LaravelApplication.hasUnconfiguredSailComponents(): Boolean {
    val configurationProviders = SailConfigurationProvider.EP_NAME.extensionList

    return configurationProviders.any { !it.configurationExists(this) }
}