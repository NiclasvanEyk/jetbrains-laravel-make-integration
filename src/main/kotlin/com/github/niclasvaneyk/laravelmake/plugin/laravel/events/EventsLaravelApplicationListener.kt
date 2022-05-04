package com.github.niclasvaneyk.laravelmake.plugin.laravel.events

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.EventBusConnector
import com.intellij.openapi.progress.runBackgroundableTask

class EventsLaravelApplicationListener: LaravelApplicationListener {
    override fun initialized(application: LaravelApplication) {
        runBackgroundableTask("Laravel Make IDE Connector", application.project) {
            EventBusConnector(application) { it.isCanceled }.install()
        }
    }

    override fun event(name: String) {}
}