package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.seeders

import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.extends
import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.isPhpIdentifier
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRunConfigurationType
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer

class SeederRunConfigurationProducer: LazyRunConfigurationProducer<PhpScriptRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory {
        return PhpScriptRuntimeConfigurationProducer().configurationFactory,
    }

    override fun isConfigurationFromContext(
        configuration: PhpScriptRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        return true
    }

    override fun setupConfigurationFromContext(
        configuration: PhpScriptRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val element = sourceElement.get()

        if (!element.isPhpIdentifier()) return false

        val `class` = element.parent
        if (`class` !is PhpClass) return false
        if (`class`.isAbstract) return false
        if (!`class`.extends("\\Illuminate\\Database\\Seeder")) return false

        val project = element.project
        val laravel = project.laravel() ?: return false
        configuration.settings.apply {
            path = laravel.paths.path("artisan")

        }
//        configuration.
    }
}

class SeederRunLineMarkerContributor: RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        if (!element.isPhpIdentifier()) return null

        val `class` = element.parent
        if (`class` !is PhpClass) return null
        if (`class`.isAbstract) return null
        if (!`class`.extends("\\Illuminate\\Database\\Seeder")) return null

        val project = element.project
        val laravel = project.laravel() ?: return null
        val actionName = "Run '${`class`.name}'"

        return Info(object : AnAction({ actionName }, Run) {
            override fun actionPerformed(e: AnActionEvent) {
                val runConfiguration = PhpScriptRunConfiguration(
                    project,
                    PhpScriptRuntimeConfigurationProducer().configurationFactory,
                    actionName,
                )

                val settings = RunnerAndConfigurationSettingsImpl(
                    RunManagerImpl.getInstanceImpl(project),
                    runConfiguration,
                )
                val executor = DefaultRunExecutor.getRunExecutorInstance()

                ProgramRunnerUtil.executeConfiguration(settings, executor)
            }
        })
    }
}