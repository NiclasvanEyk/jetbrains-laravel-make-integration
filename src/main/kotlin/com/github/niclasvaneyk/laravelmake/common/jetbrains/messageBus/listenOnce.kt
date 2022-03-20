package com.github.niclasvaneyk.laravelmake.common.jetbrains.messageBus

import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic

fun <Listener: Any> Project.listenOnce(topic: Topic<Listener>, listener: (disconnect: () -> Unit) -> Listener) {
    messageBus.connect(this).also { it.subscribe(topic, listener { it.disconnect() }) }
}