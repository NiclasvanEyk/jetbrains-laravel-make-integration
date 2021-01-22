package com.niclas_van_eyk.laravel_make_integration.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.project.Project
import com.intellij.util.PathUtil
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer

/**
 * Represents an execution of a php script, like the artisan script.
 *
 * This uses the Jetbrains Run-Configurations, to be able to execute scripts using the
 * configured project interpreter, which enables the use of docker containers, etc.
 */
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
        interpreter = InterpreterInference(project).inferInterpreter()
        command = buildCommand()
        processHandler = buildProcessHandler()
        processListener = attachProcessListener(processHandler)
    }

    fun run(): Result {
        processHandler.startNotify()

        return Result(processHandler.waitFor() && processHandler.exitCode == 0, processListener.texts)
    }

    open class Result(
            private val success: Boolean,
            private val logs: List<String> = emptyList()) {
        val wasSuccessful: Boolean get() = success
        val wasFailure: Boolean get() = !success
        val log: String get() = logs.joinToString("\n")
        val logWithoutNewLines: String get() = logs.joinToString("")
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
}

class NoInterpreterSetException: Exception()