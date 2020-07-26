package com.niclas_van_eyk.laravel_make_integration.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.project.Project
import com.intellij.util.PathUtil
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer

class PHPScriptRun(
    private val path: String,
    private val arguments: Iterable<String>,
    private val project: Project
) {
    private val processHandler: ProcessHandler
    private val processListener: PHPScriptRunListener
    private val runConfiguration: PhpScriptRunConfiguration
    private val command: PhpCommandSettings
    private val interpreter: PhpInterpreter

    init {
        runConfiguration = buildRunConfiguration()
        interpreter = inferInterpreter()
        command = buildCommand()
        processHandler = buildProcessHandler()
        processListener = attachProcessListener(processHandler)
    }

    fun run(): Result {
        processHandler.startNotify()

        return if (!processHandler.waitFor() || processHandler.exitCode != 0) {
            Result.Failure(processListener.texts)
        } else {
            Result.Success()
        }
    }

    open class Result {
        class Success: Result()

        class Failure(private val logs: List<String>): Result() {
            val log: String get() = logs.joinToString("\n")
        }
    }

    private fun buildProcessHandler(): ProcessHandler {
        val processHandler = runConfiguration.createProcessHandler(project, command)
        ProcessTerminatedListener.attach(processHandler, project, "Done!")

        return processHandler
    }

    private fun attachProcessListener(processHandler: ProcessHandler): PHPScriptRunListener {
        val listener = PHPScriptRunListener()
        processHandler.addProcessListener(listener)

        return listener
    }

    private fun buildCommand(): PhpCommandSettings {
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

    private fun inferInterpreter(): PhpInterpreter {
        val configuredInterpreter = PhpProjectConfigurationFacade
            .getInstance(project)
            .interpreter

        if (configuredInterpreter != null) {
            return configuredInterpreter
        }

        // It could be, that the user has not specified an interpreter.
        // If we find one locally, we will just use that and notify the
        // user, that they should specify an interpreter for the project,
        // so that the nasty message goes away.
        // We choose a local interpreter, because the remote ones could
        // be pointing to a server via ssh, so we do not be responsible
        // for any unwanted side-effects.
        val localInterpreter = inferLocalInterpreter()

        return localInterpreter ?: throw NoInterpreterSetException()
    }

    private fun inferLocalInterpreter(): PhpInterpreter? {
        return PhpInterpretersManagerImpl.getInstance(project)
            .interpreters
            .first { !it.isRemote }
    }
}

class NoInterpreterSetException: Exception()