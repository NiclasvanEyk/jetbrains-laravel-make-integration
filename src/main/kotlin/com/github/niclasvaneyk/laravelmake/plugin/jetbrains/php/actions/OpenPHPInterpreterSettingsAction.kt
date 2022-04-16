package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.jetbrains.php.config.interpreters.PhpInterpretersConfigurable

/**
 * Opens the settings for the project PHP interpreter(s).
 */
class OpenPHPInterpreterSettingsAction : DumbAwareAction("Open Settings") {
    private val settings get() = ShowSettingsUtil.getInstance()

    override fun actionPerformed(e: AnActionEvent) {
        settings.showSettingsDialog(
            e.project,
            PhpInterpretersConfigurable.PROJECT_CONFIGURABLE.name,
        )
    }
}
