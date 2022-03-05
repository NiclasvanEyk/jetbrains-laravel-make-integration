package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.run

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.Gaps
import com.jetbrains.php.run.PhpRunConfiguration
import com.jetbrains.php.run.PhpRunConfigurationFactoryBase

class ArtisanRunSettings(
    var command: String = "",
    var subCommand: String = "",
    var arguments: List<String> = emptyList(),
) {
    fun fillFrom(other: ArtisanRunSettings) {
        this.command = other.command
        this.subCommand = other.subCommand
        this.arguments = other.arguments
    }
}

/**
 * This was mostly inspired by https://sourcegraph.com/github.com/JetBrains/intellij-community/-/blob/plugins/gradle/src/org/jetbrains/plugins/gradle/service/execution/GradleDebugSettingsEditor.kt
 */
@Suppress("UnstableApiUsage")
class ArtisanRunSettingsEditor: SettingsEditor<ArtisanRunConfiguration>() {
    private lateinit var commandTextField: JBTextField
    private lateinit var subCommandTextField: JBTextField
    private lateinit var argumentsTextField: JBTextField

    override fun resetEditorFrom(s: ArtisanRunConfiguration) {
        commandTextField.text = s.settings.command
        subCommandTextField.text = s.settings.subCommand
        argumentsTextField.text = s.settings.arguments.joinToString { " " }
    }

    override fun applyEditorTo(s: ArtisanRunConfiguration) {
        s.settings.apply {
            command = commandTextField.text
            subCommand = subCommandTextField.text
            arguments = argumentsTextField.text.split(" ")
        }
    }

    override fun createEditor() = panel {
        row {
            textField()
                .applyToComponent { commandTextField = this }
                .label("Command")
                .customize(Gaps.EMPTY)

            textField()
                .applyToComponent { subCommandTextField = this }
                .label(":")
                .customize(Gaps.EMPTY)
        }
        row {
            textField()
                .applyToComponent { argumentsTextField = this }
                .label("Arguments")
        }
    }
}

class ArtisanRunConfigurationType: ConfigurationType, DumbAware {
    companion object {
        fun instance() = ConfigurationTypeUtil.findConfigurationType(ArtisanRunConfigurationType::class.java)
    }

    val factory = object: PhpRunConfigurationFactoryBase(this, displayName) {
        override fun isEditableInDumbMode() = true

        override fun createTemplateConfiguration(project: Project): RunConfiguration {
            return ArtisanRunConfiguration(project, this, "")
        }
    }

    override fun getId() = "laravel-make-artisan-run-configuration"
    override fun getDisplayName() = "Artisan Command"
    override fun getConfigurationTypeDescription() = "Run an artisan command"
    override fun getIcon() = LaravelIcons.LaravelLogo

    override fun getConfigurationFactories() = arrayOf(factory)
}

class ArtisanRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
): PhpRunConfiguration<ArtisanRunSettings>(project, factory, name) {
    override fun getConfigurationEditor() = ArtisanRunSettingsEditor()
    override fun createSettings() = ArtisanRunSettings()

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return RunProfileState { executor, runner ->
            return@RunProfileState DefaultExecutionResult()
        }
    }
}