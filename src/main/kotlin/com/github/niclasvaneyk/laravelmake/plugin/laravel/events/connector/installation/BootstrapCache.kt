package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.installation

import com.github.niclasvaneyk.laravelmake.common.filesystem.hasParents
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File

/**
 * Adjustments to the `bootstrap/cache` directory needed to install the event
 * bus.
 *
 * In order for Laravel to automatically run our code needed to pickup events
 * from inside the application, we need to register a ServiceProvider. Normally
 * ServiceProviders are discovered after `composer install` is run and cached
 * into the `boostrap/cache` directory. We manually patch the files inside this
 * directory to also include our ServiceProvider and update them when needed.
 */
class BootstrapCache(
    private val providerName: String,
    private val scriptFileName: String,
    private val servicesFile: File,
    private val packagesFile: File,
) {
    fun update() {
        installToServiceFile()
        installToPackageFile()
    }

    fun updateWhenChanged(project: Project) {
        VirtualFileManager.getInstance().addAsyncFileListener({
            val cacheChanged = it.any { event ->
                val file = event.file ?: return@any false
                val correctDirectory = file.hasParents("bootstrap", "cache")
                val correctFile = listOf("services.php", "packages.php").contains(file.name)

                correctDirectory && correctFile
            }

            if (cacheChanged) {
                update()
            }

            null
        }, project)
    }

    private fun installToServiceFile() {
        val currentServices = servicesFile.readText()
        if (currentServices.contains(providerName)) return

        val newServices = currentServices
            // We replace it two times, once for the definition of the service
            // provider, and once for defining it as 'eager'
            .replaceFirst("  ),", "  99999 => '$providerName',),")
            .replaceFirst("  ),", "  99999 => '$providerName',),")

        servicesFile.writeText(newServices)
    }

    private fun installToPackageFile() {
        val currentPackages = packagesFile.readText()
        if (currentPackages.contains(providerName)) return

        val newPackages = currentPackages
            // We create an entry for a pseudo-package containing the
            // ServiceProvider
            .replace(");", """
                  // Generated by Laravel Make extension
                  'niclas-van-eyk/laravel-make-ide-connector' =>
                  array (
                    'providers' => array (
                      0 => '$providerName',
                    ),
                  ),
                );
            """.trimIndent())
            // we also need to make sure the class is actually loaded
            .replace(
                "<?php ",
                "<?php if (file_exists(__DIR__ . '/../../vendor/_laravel_make/$scriptFileName')) { require_once(__DIR__ . '/../../vendor/_laravel_make/$scriptFileName'); } "
            )

        packagesFile.writeText(newPackages)
    }
}