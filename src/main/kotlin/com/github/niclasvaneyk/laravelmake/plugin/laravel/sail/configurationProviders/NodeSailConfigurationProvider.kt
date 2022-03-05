package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider

class NodeSailConfigurationProvider: SailConfigurationProvider {
    companion object {
        /**
         * The path inside the sail container that needs to be configured.
         */
        const val NPM_PATH = "/usr/lib/node_modules/npm"
    }

    override fun apply(application: LaravelApplication) {

    }
}