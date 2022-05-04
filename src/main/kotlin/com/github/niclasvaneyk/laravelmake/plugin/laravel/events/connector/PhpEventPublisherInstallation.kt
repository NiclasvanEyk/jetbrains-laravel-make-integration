package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector

import com.intellij.openapi.Disposable
import org.newsclub.net.unix.AFUNIXServerSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.File

class PhpEventPublisherInstallation (private val socketFile: File): Disposable {
    private lateinit var socket: AFUNIXServerSocket

    fun listen(isCancelled: () -> Boolean, onEvent: (event: String) -> Unit) {
        socket = AFUNIXServerSocket.newInstance()
        socket.bind(AFUNIXSocketAddress(socketFile))

        while (!isCancelled()) {
            val s = socket.accept()
            val event = String(s.inputStream.readAllBytes())

            onEvent(event)
        }

        socket.close()
    }

    override fun dispose() {
        if (this::socket.isInitialized) {
            this.socket.close()
        }
    }
}