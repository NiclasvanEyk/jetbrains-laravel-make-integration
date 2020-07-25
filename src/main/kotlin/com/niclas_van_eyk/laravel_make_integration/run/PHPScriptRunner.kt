package com.niclas_van_eyk.laravel_make_integration.run

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.util.PathUtil
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.PhpRuntimeConfiguration
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.run.PhpInterpreterSettings
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import java.util.*

class PHPScriptRunner {
    fun run(path: String, arguments: Iterable<String>, project: Project) {
        val runConfiguration = PhpScriptRunConfiguration(
                project,
                PhpScriptRuntimeConfigurationProducer().configurationFactory,
                "test"
        )

        val interpreter = PhpProjectConfigurationFacade
                .getInstance(project)
                .interpreter

        if (interpreter == null) {
            // TODO: Notify user to set a interpreter!
            return
        }

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

        val processHandler = runConfiguration.createProcessHandler(project, command)

//        val consoleBuilder = TextConsoleBuilderFactory
//                .getInstance()
//                .createBuilder(project)
//        consoleBuilder.setViewer(true)
//
//        val console = consoleBuilder.console
//
//        console.component.isVisible = true

        val listener = PHPScriptRunListener()
        processHandler.addProcessListener(listener)

//        console.attachToProcess(processHandler)
        ProcessTerminatedListener.attach(processHandler, project, "Done!")
        processHandler.startNotify()

        if (!processHandler.waitFor() || processHandler.exitCode != 0) {
            throw ScriptRunFailedException(listener)
        }
    }
}
