package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import java.io.File

/**
 * Adjustments to the `vendor` directory needed to install the event
 * bus.
 *
 * In order for Laravel to automatically run our code needed to pickup events
 * from inside the application, we need to register a ServiceProvider. This
 * gets created in the `vendor/` directory, which is the responsibility of
 * this class.
 */
class VendorPseudoPackage(
    private val connectorPhpScript: File,
    private val connectorScriptContents: String,
    private val app: LaravelApplication,
) {
    private val virtualFileManager: VirtualFileManager get() {
        return VirtualFileManager.getInstance()
    }

    private val listener = AsyncFileListener { events ->
        val vendorDirectoryPath = app.paths.path("vendor")
        val vendorDirectoryWasCreated = events.any { event ->
            if (event !is VFileCreateEvent) {
                return@any false
            }
            val file = event.file ?: return@any false
            file.path == vendorDirectoryPath
        }

        if (vendorDirectoryWasCreated) {
            create()
        }

        null
    }

    private val listenerDisposable  = Disposable {
        // This is just a dummy disposable, so we can manually dispose
        // the listener when explicitly uninstalling.
    }

    fun create() {
        // TODO: Check Version and overwrite if necessary?
        if (!connectorPhpScript.isFile) {
            connectorPhpScript.writeText(connectorScriptContents)
        }
    }

    fun remove() {
        if (connectorPhpScript.isFile) {
            connectorPhpScript.delete()
        }
    }

    fun createWhenComposerInstalled() {
        virtualFileManager.addAsyncFileListener(listener, listenerDisposable)
        Disposer.register(app.project, listenerDisposable)
    }

    fun removeComposerInstallationListener() {
        Disposer.dispose(listenerDisposable)
    }
}