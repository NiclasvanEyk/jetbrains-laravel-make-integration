package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import java.io.File

/**
 * Actually connects via a unix domain socket to the Laravel application,
 * to be able to listen to events. This way, we can _always_ notice, e.g.
 * when migrations are run and react accordingly (e.g. refresh some
 * data sources).
 */
class EventBusConnector(
    private val app: LaravelApplication,
    private val isCanceled: () -> Boolean,
) {
    private val socket = app.paths.pluginFiles.resolve("ide_connector.socket")

    fun install() {
        val publisher = PhpEventPublisher(app, socket).install()

        publisher.listen(isCanceled) {
            it.lines().forEach { LaravelApplicationListener.publishEvent(it) }
        }
    }
}