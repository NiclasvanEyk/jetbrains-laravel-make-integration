package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMake
import com.intellij.openapi.diagnostic.logger

interface LaravelApplicationListener {
    companion object {
        val EP_NAME = LaravelMake.extensionName<LaravelApplicationListener>("laravelApplicationListener")

        fun initializeAll(app: LaravelApplication) {
            EP_NAME.extensionList.forEach {
                try {
                    it.initialized(app)
                } catch (exception: Throwable) {
                    logger<LaravelApplication>().error(exception)
                }
            }
        }

        fun publishEvent(event: String) {
            EP_NAME.extensionList.forEach {
                try {
                    it.event(event)
                } catch (exception: Throwable) {
                    logger<LaravelApplication>().error(exception)
                }
            }
        }
    }

    /**
     * Will be called after a project was opened that contains a Laravel application
     * and we made sure that the user has configured a valid project interpreter.
     */
    fun initialized(application: LaravelApplication)

    /**
     * Will be called when an event happens.
     */
    fun event(name: String)
}