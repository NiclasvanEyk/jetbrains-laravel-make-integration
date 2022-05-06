package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation

import com.github.niclasvaneyk.laravelmake.common.filesystem.resource
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import java.io.File

/**
 * Installs components needed to retrieve events from the running Laravel app.
 */
class PhpEventPublisherInstaller(private val app: LaravelApplication) {
    companion object {
        const val SCRIPT_FILE_NAME = "ide_connector.php"
        const val SERVICE_PROVIDER_NAME = "LaravelMakeIdeConnectorServiceProvider"
    }

    private val cacheDirectory = File(app.paths.path("bootstrap/cache"))
    private val connectorPhpScript = app.paths.pluginFiles.resolve(SCRIPT_FILE_NAME)
    private val connectorScriptContents = resource("/php/$SCRIPT_FILE_NAME")

    private val bootstrapCache = BootstrapCache(
        SERVICE_PROVIDER_NAME,
        SCRIPT_FILE_NAME,
        cacheDirectory.resolve("services.php"),
        cacheDirectory.resolve("packages.php"),
    )
    private val vendorPseudoPackage = VendorPseudoPackage(
        connectorPhpScript,
        connectorScriptContents,
    )

    fun install(): PhpEventPublisherInstallation {
        app.paths.pluginFiles.mkdirs()

        if (File(app.paths.path("vendor")).exists()) {
            vendorPseudoPackage.create()
            bootstrapCache.update()
        }

        vendorPseudoPackage.createWhenComposerInstalled(app)
        bootstrapCache.updateWhenChanged(app.project)

        return PhpEventPublisherInstallation(app.project)
    }
}
