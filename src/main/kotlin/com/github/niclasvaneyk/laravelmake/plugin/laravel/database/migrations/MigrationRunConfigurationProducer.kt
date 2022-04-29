package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.ConfigurationFromContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer

/**
 * Enables migration files to be run (through artisan migrate), if they have
 * not been run yet.
 */
class MigrationRunConfigurationProducer: LazyRunConfigurationProducer<PhpScriptRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory {
        return PhpScriptRuntimeConfigurationProducer().configurationFactory
    }

    override fun isConfigurationFromContext(
        configuration: PhpScriptRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val element = context.location?.psiElement ?: return false
        element.migration ?: return false
        val app = element.project.laravel() ?: return false

        val pathMatches = app.paths.path("artisan") == configuration.settings.path
        val paramsMatch = "artisan migrate" == configuration.settings.scriptParameters
        return pathMatches && paramsMatch
    }

    override fun setupConfigurationFromContext(
        configuration: PhpScriptRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val element = sourceElement.get()
        val app = element.project.laravel() ?: return false
        val migration = element.migration ?: return false
        if (migration.ran) return false

        configuration.name = "artisan migrate"
        configuration.settings.apply {
            path = app.paths.path("artisan")
            setScriptParameters("migrate")
        }

        return true
    }
}
