package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolWindow

import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.JBColor
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

open class MasterDetailToolWindow : SimpleToolWindowPanel(false) {
    private val splitter = OnePixelSplitter().apply {
        lackOfSpaceStrategy = Splitter.LackOfSpaceStrategy.HONOR_THE_FIRST_MIN_SIZE
        setResizeEnabled(true)
    }

    var master: JComponent? = null
        set(value) {
            field = value
            splitter.firstComponent = value
        }

    var detail: JComponent? = null
        set(value) {
            field = value
            splitter.secondComponent = value
        }

    init {
        add(splitter)
    }
}
