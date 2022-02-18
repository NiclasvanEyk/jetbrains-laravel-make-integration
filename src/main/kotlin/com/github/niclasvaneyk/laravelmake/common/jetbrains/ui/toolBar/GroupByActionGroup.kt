package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.DefaultActionGroup

class GroupByActionGroup : DefaultActionGroup() {
    init {
        templatePresentation.apply {
            text = "Group By"
            icon = AllIcons.Actions.GroupBy
        }
        isPopup = true
        addSeparator("Group By")
    }
}
