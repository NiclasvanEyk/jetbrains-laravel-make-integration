package com.niclas_van_eyk.laravel_make_integration.common.laravel

import com.niclas_van_eyk.laravel_make_integration.common.php.run.PHPRunner
import java.io.File

/**
 * Wrapper to execute artisan commands.
 *
 * Should represent the artisan-binary, still need to figure out a good way to
 * save the project here, so it does not need to be passed for each method...
 */
class Artisan(
    private val basePath: String,
    private val runner: PHPRunner,
) {
    companion object {
        fun existsAt(path: String) = File(path).exists()
        fun binaryPath(path: String) ="${path}${LaravelProjectPaths.ARTISAN}"
    }

    fun make(
        subCommand: String,
        parameters: ArtisanMakeParameters
    ): PHPRunner.Result {
        return command("make", subCommand, parameters.asList())
    }

    fun command(
        namespace: String,
        subCommand: String? = null,
        parameters: Collection<String> = emptyList()
    ): PHPRunner.Result {
        val command = if (subCommand != null) "$namespace:$subCommand" else namespace

        val params = mutableListOf(command)

        if (parameters.isNotEmpty()) {
            params.addAll(parameters)
        }

        return runner.executeScript(binaryPath(basePath), params)
    }
}
