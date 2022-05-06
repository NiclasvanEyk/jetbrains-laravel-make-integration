package com.github.niclasvaneyk.laravelmake.plugin.laravel.events

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.events
import com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation.PhpEventPublisherInstaller

class EventsLaravelApplicationListener: LaravelApplicationListener {
    override fun initialized(application: LaravelApplication) {
        PhpEventPublisherInstaller(application).install().listen { eventBatch ->
            eventBatch.lines().forEach { event ->
                application.events.dispatch(event)
            }
        }
    }
}