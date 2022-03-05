package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMake
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication

interface SailConfigurationProvider {
    companion object {
        val EP_NAME = LaravelMake.extensionName<SailConfigurationProvider>("laravelSailConfigurationProvider")
    }

    fun apply(application: LaravelApplication)
}