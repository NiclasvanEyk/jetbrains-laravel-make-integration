package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.jetbrains.php.config.interpreters.PhpInterpretersConfigurable

/**
 * Opens the settings for the project PHP interpreter(s).
 */
class OpenPHPInterpreterSettingsAction : AnAction("Open Settings") {
    private val settings get() = ShowSettingsUtil.getInstance()

    override fun actionPerformed(e: AnActionEvent) {
        settings.showSettingsDialog(
            e.project,
            PhpInterpretersConfigurable.PROJECT_CONFIGURABLE.name,
        )
    }
}
