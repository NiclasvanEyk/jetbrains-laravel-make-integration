package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener

class SailLaravelApplicationListener: LaravelApplicationListener {
    /**
     * If necessary, show a notification that helps the user with setting up
     * the IDE for development using Laravel Sail.
     */
    override fun initialized(application: LaravelApplication) {
        SailAutoconfiguration.promptIfNecessary(application)
    }

    override fun event(name: String) {}
}