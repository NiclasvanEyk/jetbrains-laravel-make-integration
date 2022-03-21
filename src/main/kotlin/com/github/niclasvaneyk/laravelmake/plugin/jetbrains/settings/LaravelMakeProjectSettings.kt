package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailAutoconfiguration
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

val Project.settings get() = this.service<LaravelMakeProjectSettings>().state
val LaravelApplication.settings get() = project.settings

enum class DisplayUnAvailableActionsInContextMenuStrategy {
    Hide,
    Disable,
    Keep,
}

/**
 * Holds various settings for this plugin.
 *
 * Should be retrieved through the [LaravelMakeProjectSettings] service.
 */
data class LaravelMakeProjectSettingsState(
    /**
     * See [SailAutoconfiguration.shouldBeShownFor].
     */
    var shouldDisplaySailAutoconfigurationPopup: Boolean = true,

    /**
     * What to do with actions not matching the current path when using the "File > New > Laravel" Make actions.
     */
    var displayUnAvailableActionsInContextMenuStrategy: DisplayUnAvailableActionsInContextMenuStrategy = DisplayUnAvailableActionsInContextMenuStrategy.Disable,
) {
    fun fill(other: LaravelMakeProjectSettingsState) {
        shouldDisplaySailAutoconfigurationPopup = other.shouldDisplaySailAutoconfigurationPopup
        displayUnAvailableActionsInContextMenuStrategy = other.displayUnAvailableActionsInContextMenuStrategy
    }
}

/**
 * Manages access and serialization of the projects [LaravelMakeProjectSettingsState].
 */
@State(name = "LaravelMakeProjectSettings", storages = [Storage("laravel-make.xml")])
class LaravelMakeProjectSettings: PersistentStateComponent<LaravelMakeProjectSettingsState> {
    private val _state = LaravelMakeProjectSettingsState()
    override fun getState() = _state
    override fun loadState(state: LaravelMakeProjectSettingsState) = XmlSerializerUtil.copyBean(state, this._state)
}