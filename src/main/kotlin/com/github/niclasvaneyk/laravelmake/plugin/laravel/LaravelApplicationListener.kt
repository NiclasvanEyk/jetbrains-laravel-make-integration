package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMake

interface LaravelApplicationListener {
    companion object {
        val EP_NAME = LaravelMake.extensionName<LaravelApplicationListener>("laravelApplicationListener")
    }

    /**
     * Will be called after a project was opened that contains a Laravel application
     * and we made sure that the user has configured a valid project interpreter.
     */
    fun initialized(application: LaravelApplication)
}