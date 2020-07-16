package com.niclas_van_eyk.laravel_make_integration.run

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.util.PathUtil
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer

class PHPScriptRunner {
    fun run(path: String, arguments: List<String>, project: Project) {
        val runConfiguration = PhpScriptRunConfiguration(
                project,
                PhpScriptRuntimeConfigurationProducer().configurationFactory,
                "test"
        )
        val settings = runConfiguration.settings
        settings.path = path

        val interpreter = PhpInterpretersManagerImpl
                .getInstance(project)
                .interpreters.first { it.isProjectLevel }

        val command = PhpCommandSettingsBuilder(project, interpreter).build()

        command.setScript(path, true)
        command.importCommandLineSettings(
                settings.commandLineSettings,
                PathUtil.getParentPath(path)
        )
        command.addArguments(arguments)

        val processHandler = runConfiguration.createProcessHandler(project, command)

        val console = TextConsoleBuilderFactory
                .getInstance()
                .createBuilder(project)
                .console

        console.attachToProcess(processHandler)
        ProcessTerminatedListener.attach(processHandler, project)
        processHandler.startNotify()
    }
}
