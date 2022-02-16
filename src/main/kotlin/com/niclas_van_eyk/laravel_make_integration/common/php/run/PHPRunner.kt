package com.niclas_van_eyk.laravel_make_integration.common.php.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.project.Project
import com.intellij.util.PathUtil
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.run.PhpEditInterpreterExecutionException
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import com.niclas_van_eyk.laravel_make_integration.common.php.InterpreterInference

/**
 * Represents an execution of a php script, like the artisan script.
 *
 * This uses the Jetbrains Run-Configurations, to be able to execute scripts using the
 * configured project interpreter, which enables the use of docker containers, etc.
 */
class PHPRunner(private val project: Project) {
    private lateinit var runConfiguration: PhpScriptRunConfiguration
    private lateinit var interpreter: PhpInterpreter
    private var initialized = false

    fun executeScript(path: String, arguments: Iterable<String>): Result {
        if (!initialized) {
            runConfiguration = buildRunConfiguration()
            interpreter = InterpreterInference(project).inferInterpreter()
            initialized = true
        }

        val command: PhpCommandSettings
        try {
            command = buildCommand(path, arguments)
        } catch (exception: ExecutionException) {
            if (exception.localizedMessage.startsWith("PHP home")) {
                throw InvalidInterpreterException(exception)
            } else {
                throw exception
            }
        }

        val processHandler = buildProcessHandler(command)
        val processListener = attachProcessListener(processHandler)

        processHandler.startNotify()

        try {
            return Result(
                processHandler.waitFor() && processHandler.exitCode == 0,
                processListener.texts
            )
        } catch (e: PhpEditInterpreterExecutionException) {
            if (e.message?.contains("Docker") == true) {
                throw DockerNotStartedException(e)
            } else {
                throw RemoteInterpreterNotAvailableException(e)
            }
        } catch (e: ExecutionException) {
            when (e.message) {
                PhpCommandSettingsBuilder.getProjectInterpreterNotSpecified() ->
                    throw NoProjectInterpreterException(e)

                PhpCommandSettingsBuilder.getInterpreterNotFoundError() ->
                    throw ProjectInterpreterNotFoundException(e)

                else -> throw PHPScriptRunException(
                    "Something went wrong while running '" +
                        arguments.joinToString(" ") +
                        "'",
                    e
                )
            }
        }
    }

    open class Result(
        private val success: Boolean,
        private val logs: List<String> = emptyList()
    ) {
        val wasSuccessful: Boolean get() = success
        val wasFailure: Boolean get() = !success
        val log: String get() = logs.joinToString("\n")
        val logWithoutNewLines: String get() = logs.joinToString("")
    }

    private fun buildProcessHandler(command: PhpCommandSettings): ProcessHandler {
        val processHandler: ProcessHandler
        try {
            processHandler = runConfiguration.createProcessHandler(project, command)
        } catch (exception: PhpEditInterpreterExecutionException) {
            if (exception.localizedMessage.toLowerCase().contains("docker")) {
                throw DockerNotStartedException(exception)
            } else {
                throw exception
            }
        }
        ProcessTerminatedListener.attach(processHandler, project, "Done!")

        return processHandler
    }

    private fun attachProcessListener(
        processHandler: ProcessHandler
    ): PHPScriptRunListener {
        val listener = PHPScriptRunListener()
        processHandler.addProcessListener(listener)

        return listener
    }

    private fun buildCommand(
        path: String,
        arguments: Iterable<String>,
    ): PhpCommandSettings {
        val command = PhpCommandSettingsBuilder(project, interpreter).build()

        command.setScript(path, true)
        val settings = runConfiguration.settings
        settings.path = path
        settings.setScriptParameters(arguments.joinToString(" "))
        command.importCommandLineSettings(
            settings.commandLineSettings,
            PathUtil.getParentPath(path)
        )
        command.addArguments(arguments.toList())

        return command
    }

    private fun buildRunConfiguration(): PhpScriptRunConfiguration {
        return PhpScriptRunConfiguration(
            project,
            PhpScriptRuntimeConfigurationProducer().configurationFactory,
            "Laravel Make Integration"
        )
    }
}

class NoInterpreterSetException : Exception()
