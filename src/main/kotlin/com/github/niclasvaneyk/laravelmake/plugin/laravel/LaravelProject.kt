package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.google.common.collect.ImmutableList
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.LaravelProjectPaths
import com.github.niclasvaneyk.laravelmake.common.laravel.DetectLaravelVersion
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunner
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunnerFactory
import com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.LaravelIntrospectionFacade
import java.io.File

class LaravelProject(path: String, val jetbrainsProject: Project) {
    val artisan: Artisan = Artisan(path, PHPRunnerFactory(jetbrainsProject))
    val paths: LaravelProjectPaths = LaravelProjectPaths(path)
    val version = DetectLaravelVersion.fromLockfile(
        File(paths.path(LaravelProjectPaths.COMPOSER_LOCK))
    ) ?: ComposerVersion(0, 0, 0)
    val introspection = LaravelIntrospectionFacade(
        artisan,
        ProgressBarBuilder(jetbrainsProject),
        jetbrainsProject,
    )

    constructor(base: VirtualFile, jetbrainsProject: Project) :
        this(base.path, jetbrainsProject)
}

/**
 * Responsible for validating and creating a [LaravelProject].
 */
class LaravelProjectFactory(private val jetbrainsProject: Project) {
    private val _errors = ArrayList<String>()
    val errors: ImmutableList<String>
        get() = ImmutableList.copyOf(_errors)

    fun build(): LaravelProject? {
        val path = jetbrainsProject.basePath!!

        if (!File(path).exists()) {
            addError("'$path' does not exist!")
            return null
        }

        if (!File(path).isDirectory) {
            addError("'$path' is not a directory and therefore can't be a base for a Laravel project!")
            return null
        }

        if (!Artisan.existsAt(path)) {
            addError("No artisan binary found in directory '$path'!")
            return null
        }

        return LaravelProject(path, jetbrainsProject)
    }

    private fun addError(message: String) {
        _errors.add(message)
    }
}
