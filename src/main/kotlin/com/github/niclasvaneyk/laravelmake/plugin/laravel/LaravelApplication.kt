package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.DetectLaravelVersion
import com.github.niclasvaneyk.laravelmake.common.laravel.LaravelProjectPaths
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunnerFactory
import com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.LaravelIntrospectionFacade
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.launchUnderModalProgress
import com.jetbrains.rd.util.lifetime.Lifetime
import java.io.File

class LaravelApplication(path: String, val project: Project) {
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
        validateProjectInterpreter(this@LaravelApplication).then {
            introspection.commandIntrospecter.refresh()
        }
    }
}
