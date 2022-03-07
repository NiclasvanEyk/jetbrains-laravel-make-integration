package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMake
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication

interface SailConfigurationProvider {
    companion object {
        val EP_NAME = LaravelMake.extensionName<SailConfigurationProvider>("laravelSailConfigurationProvider")
    }

    /**
     * Run the automated setup tasks.
     */
    fun apply(application: LaravelApplication)

    /**
     * Whether any automated setup tasks even need to run.
     */
    fun configurationExists(application: LaravelApplication): Boolean
}