package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import java.util.UUID

internal typealias ListenerMap = MutableMap<String, MutableMap<UUID, () -> Unit>>

val LaravelApplication.events: EventBus get() {
    return project.service()
}

/**
 * Mini-implementation of the Laravel EventBus, that is actually connected
 * to the application.
 *
 * This class only stores the listeners, the interesting implementation is
 * located in the installation package.
 */
@Service(Service.Level.PROJECT)
class EventBus {
    private val listeners: ListenerMap = mutableMapOf()

    /**
     * Listens to event with the exact [name] and calls [callback] when emitted
     * by the Laravel application.
     *
     * @return A callback to unregister the listener.
     */
    fun subscribe(name: String, callback: () -> Unit): () -> Unit {
        val listenerMap = listeners.computeIfAbsent(name) { mutableMapOf() }
        val listenerId = UUID.randomUUID()
        listenerMap[listenerId] = callback

        return { listenerMap.remove(listenerId) }
    }

    fun dispatch(name: String) = listeners[name]?.values?.forEach { it() }
}