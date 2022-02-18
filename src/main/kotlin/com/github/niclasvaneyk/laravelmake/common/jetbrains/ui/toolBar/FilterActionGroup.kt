package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.DefaultActionGroup

open class FilterActionGroup : DefaultActionGroup() {
    init {
        templatePresentation.apply {
            text = "Filter"
            icon = AllIcons.General.Filter
        }
        isPopup = true
        addSeparator("Filter")
    }
}
