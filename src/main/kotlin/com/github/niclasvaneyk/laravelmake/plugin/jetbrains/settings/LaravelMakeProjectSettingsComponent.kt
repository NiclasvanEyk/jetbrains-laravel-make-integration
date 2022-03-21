package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import javax.swing.JComboBox
import javax.swing.JPanel

/**
 * UI components for editing the projects [LaravelMakeProjectSettingsState].
 */
class LaravelMakeProjectSettingsComponent(initialValues: LaravelMakeProjectSettingsState) {
    private val form get() = FormBuilder.createFormBuilder()
    private val sailAutoconfigurationCheckBox = JBCheckBox("Offer Sail Autoconfiguration")
    private val unAvailableActionsInContextMenuStrategyDropdown = ComboBox(DisplayUnAvailableActionsInContextMenuStrategy.values())

    val settingsDerivedFromFormValues: LaravelMakeProjectSettingsState get() = LaravelMakeProjectSettingsState(
        shouldDisplaySailAutoconfigurationPopup = sailAutoconfigurationCheckBox.isSelected,
        displayUnAvailableActionsInContextMenuStrategy = unAvailableActionsInContextMenuStrategyDropdown.item
    )

    init {
        fill(initialValues)
    }

    @Suppress("DialogTitleCapitalization")
    val mainPanel: JPanel = form
        .addComponent(sailAutoconfigurationCheckBox)
        .addLabeledComponent("How to handle irrelevant artisan:make commands:", unAvailableActionsInContextMenuStrategyDropdown)
        .addComponentFillVertically(JPanel(), 0)
        .panel
    
    fun fill(settings: LaravelMakeProjectSettingsState) {
        sailAutoconfigurationCheckBox.isSelected = settings.shouldDisplaySailAutoconfigurationPopup
        unAvailableActionsInContextMenuStrategyDropdown.item = settings.displayUnAvailableActionsInContextMenuStrategy
    }
}