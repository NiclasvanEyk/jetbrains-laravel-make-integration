package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpreterComboBox
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import javax.swing.JPanel

/**
 * UI components for editing the projects [LaravelMakeProjectSettingsState].
 */
class LaravelMakeProjectSettingsComponent(
    initialValues: LaravelMakeProjectSettingsState,
    private val project: Project,
) {
    private val form get() = FormBuilder.createFormBuilder()
    private val interpreterComboBox = PhpInterpreterComboBox(project) { true }
    private val sailAutoconfigurationCheckBox = JBCheckBox("Offer Sail Autoconfiguration")
    private val defaultDatabaseAutoconfigurationCheckBox = JBCheckBox("Offer Default Database Connection Autoconfiguration")
    private val unAvailableActionsInContextMenuStrategyDropdown = ComboBox(DisplayUnAvailableActionsInContextMenuStrategy.values())

    val settingsDerivedFromFormValues: LaravelMakeProjectSettingsState get() = LaravelMakeProjectSettingsState(
        shouldDisplaySailAutoconfigurationPopup = sailAutoconfigurationCheckBox.isSelected,
        shouldDisplayDefaultDatabaseConnectionSyncBanner = defaultDatabaseAutoconfigurationCheckBox.isSelected,
        displayUnAvailableActionsInContextMenuStrategy = unAvailableActionsInContextMenuStrategyDropdown.item,
        interpreterId = interpreterComboBox.selectedItemName
    )

    init {
        fill(initialValues)
    }

    @Suppress("DialogTitleCapitalization")
    val mainPanel: JPanel = form
        .addComponent(sailAutoconfigurationCheckBox)
        .addComponent(defaultDatabaseAutoconfigurationCheckBox)
        .addLabeledComponent("How to handle irrelevant artisan:make commands:", unAvailableActionsInContextMenuStrategyDropdown)
        .addLabeledComponent("PHP Interpreter", interpreterComboBox)
        .addComponentFillVertically(JPanel(), 0)
        .panel
    
    fun fill(settings: LaravelMakeProjectSettingsState) {
        sailAutoconfigurationCheckBox.isSelected = settings.shouldDisplaySailAutoconfigurationPopup
        defaultDatabaseAutoconfigurationCheckBox.isSelected = settings.shouldDisplayDefaultDatabaseConnectionSyncBanner
        unAvailableActionsInContextMenuStrategyDropdown.item = settings.displayUnAvailableActionsInContextMenuStrategy

        val interpreters = PhpInterpretersManagerImpl.getInstance(project).interpreters
        var chosenInterpreter: PhpInterpreter? = null
        if (settings.interpreterId != null) {
            chosenInterpreter = interpreters.find { it.name == settings.interpreterId }
        }
        interpreterComboBox.setModel(interpreters, chosenInterpreter)
    }
}