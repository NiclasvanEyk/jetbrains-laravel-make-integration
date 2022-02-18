package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.DefaultActionGroup

open class ViewOptionsActionGroup : DefaultActionGroup() {
    init {
        templatePresentation.apply {
            text = "View Options"
            icon = AllIcons.Actions.Show
        }
        isPopup = true
        addSeparator("View Options")
    }
}
