package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import java.io.File

/**
 * Responsible for validating and creating a [LaravelApplication].
 */
class LaravelApplicationFactory(private val jetbrainsProject: Project) {
    fun build(): LaravelApplication? {
        val path = jetbrainsProject.basePath
        val log = logger<LaravelApplicationFactory>()

        if (path == null) {
            log.info("`project.basePath` was `null`!")
            return null
        }

        if (!File(path).exists()) {
            log.info("'$path' does not exist!")
            return null
        }

        if (!File(path).isDirectory) {
            log.info("'$path' is not a directory and therefore can't be a base for a Laravel project!")
            return null
        }

        if (!Artisan.existsAt(path)) {
            log.info("No artisan binary found in directory '$path'!")
            return null
        }

        log.info("Found artisan binary in '$path', building Laravel application...")

        return LaravelApplication(path, jetbrainsProject)
    }
}