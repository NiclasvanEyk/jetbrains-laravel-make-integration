package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.run.PHPScriptRun
import java.io.File

/**
 * Wrapper to execute artisan commands.
 *
 * Should represent the artisan-binary, still need to figure out a good way to
 * save the project here, so it does not need to be passed for each method...
 */
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
        return execute(project, "make", subCommand, parameters.asList())
    }

    fun execute(
        project: Project,
        namespace: String,
        subCommand: String? = null,
        subCommandParams: Collection<String> = emptyList()
    ): PHPScriptRun.Result {
        val command = if (subCommand != null) "$namespace:$subCommand" else namespace

        val params = mutableListOf(command)

        if (subCommandParams.isNotEmpty()) {
            params.addAll(subCommandParams)
        }

        return PHPScriptRun(binaryPath, params, project).run()
    }
}