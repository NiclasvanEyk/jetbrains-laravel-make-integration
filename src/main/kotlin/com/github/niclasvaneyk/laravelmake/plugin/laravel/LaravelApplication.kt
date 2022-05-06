package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.DetectLaravelVersion
import com.github.niclasvaneyk.laravelmake.common.laravel.LaravelProjectPaths
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunnerFactory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.LaravelIntrospectionFacade
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.File

fun Project.laravel(): LaravelApplication? {
    return service<LaravelMakeProjectService>().application
}

class LaravelApplication(path: String, val project: Project) {
    var wasInitialized = false
    val paths: LaravelProjectPaths = LaravelProjectPaths(path)
    val version = DetectLaravelVersion.fromLockfile(
        File(paths.path(LaravelProjectPaths.COMPOSER_LOCK))
    ) ?: ComposerVersion(0, 0, 0)
    val artisan: Artisan = Artisan(path, PHPRunnerFactory(project))
    val introspection = LaravelIntrospectionFacade(
        artisan,
        ProgressBarBuilder(project),
        project,
    )

    fun initialize() {
        validateProjectInterpreter(this@LaravelApplication).then { isValid ->
            if (!isValid || wasInitialized) return@then
            wasInitialized = true

            introspection.refresh()

            LaravelApplicationListener.runAll(this)
        }
    }
}
