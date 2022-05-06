package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager

/**
 * A valid installation of the component that publishes events from the Laravel
 * app to the plugin.
 *
 * Currently works by writing events to a file, which then gets picked up by a
 * listener provided by Jetbrains. I tried writing a more efficient/fast
 * implementation using sockets or named pipes, but both options are very
 * complex to get right, as confusing permission issues arose or did not
 * work within Docker containers or on Windows.
 */
class PhpEventPublisherInstallation(private val project: Project) {
    fun listen(onEvent: (event: String) -> Unit) {
        VirtualFileManager.getInstance().addAsyncFileListener({ events ->
            for (event in events) {
                val file = event.file ?: continue
                // See resources/php/ide_connector.php
                if (file.parent?.name != "_laravel_make" || file.name != "ide_connector_latest_events.txt") {
                    continue
                }

                String(file.contentsToByteArray()).lines().forEach { onEvent(it) }
            }

            null
        }, project)
    }
}