package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * Connects the settings dialog to [LaravelMakeProjectSettingsComponent].
 */
class LaravelMakeProjectConfigurable(private val project: Project): Configurable {
    override fun getDisplayName() = "Make for Laravel"
    private var settingsComponent: LaravelMakeProjectSettingsComponent? = null

    override fun createComponent(): JComponent {
        settingsComponent = LaravelMakeProjectSettingsComponent(project.settings, project)
        return settingsComponent!!.mainPanel
    }

    override fun isModified() = settingsComponent?.settingsDerivedFromFormValues != project.settings

    override fun apply() {
        project.settings.fill(settingsComponent?.settingsDerivedFromFormValues ?: return)
    }

    override fun reset() {
        settingsComponent?.fill(project.settings)
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}