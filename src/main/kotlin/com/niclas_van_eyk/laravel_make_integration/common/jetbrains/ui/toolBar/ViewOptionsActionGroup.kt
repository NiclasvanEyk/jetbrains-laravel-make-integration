package com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolBar

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
