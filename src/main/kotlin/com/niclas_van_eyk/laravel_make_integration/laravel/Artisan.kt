package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.run.PHPScriptRun
import java.io.File

class Artisan(private val basePath: String) {
    val binaryPath: String
        get() = "$basePath${LaravelProjectPaths.ARTISAN}"

    val exists: Boolean
        get() = File(binaryPath).exists()

    fun make(
        subCommand: String,
        parameters: ArtisanMakeParameters,
        project: Project
    ): PHPScriptRun.Result {
        return execute("make", subCommand, parameters.asList(), project)
    }

    fun execute(
        namespace: String,
        subCommand: String,
        subCommandParams: Collection<String>,
        project: Project
    ): PHPScriptRun.Result {
        val params = mutableListOf("$namespace:$subCommand")
        params.addAll(subCommandParams)

        return PHPScriptRun(binaryPath, params, project).run()
    }
}