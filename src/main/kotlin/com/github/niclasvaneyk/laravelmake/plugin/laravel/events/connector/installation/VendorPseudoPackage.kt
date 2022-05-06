package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
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
) {
    fun create() {
        // TODO: Check Version and overwrite if necessary?
        if (!connectorPhpScript.isFile) {
            connectorPhpScript.writeText(connectorScriptContents)
        }
    }

    fun createWhenComposerInstalled(app: LaravelApplication) {
        VirtualFileManager.getInstance().addAsyncFileListener({
            val vendorDirectoryPath = app.paths.path("vendor")
            val vendorDirectoryWasCreated = it.any { event ->
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
        }, app.project)
    }
}