package com.niclas_van_eyk.laravel_make_integration.services

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.jetbrains.php.run.PhpRunConfiguration
import com.jetbrains.php.run.script.PhpScriptDebugRunner
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRunConfigurationType
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import com.niclas_van_eyk.laravel_make_integration.LaravelMakeIntegrationBundle

class MyProjectService(project: Project) {
    init {
        println(LaravelMakeIntegrationBundle.message("projectService", project.name))

    }
}
