package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.notifications.offerSailAutoconfiguration

class SailLaravelApplicationListener: LaravelApplicationListener {
    /**
     * If necessary, show a notification that helps the user with setting up
     * the IDE for development using Laravel Sail.
     */
    override fun initialized(application: LaravelApplication) {
        if (application.usesSail() && application.hasUnconfiguredSailComponents()) {
            offerSailAutoconfiguration(application.project)
        }
    }
}