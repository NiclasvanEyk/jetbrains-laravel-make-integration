package com.github.niclasvaneyk.laravelmake.common.laravel

import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunner
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunnerFactory
import java.io.File
import java.nio.file.Path

/**
 * Wrapper to execute artisan commands.
 *
 * Should represent the artisan-binary, still need to figure out a good way to
 * save the project here, so it does not need to be passed for each method...
 */
class Artisan(
    private val basePath: String,
    private val runnerFactory: PHPRunnerFactory,
) {
    companion object {
        fun existsAt(path: String) = File(binaryPath(path)).exists()
        fun binaryPath(path: String) = Path.of(path, LaravelProjectPaths.ARTISAN).toString()
    }

    private val runner: PHPRunner get() = runnerFactory.runner()

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
