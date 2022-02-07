package com.niclas_van_eyk.laravel_make_integration.plugin.jetbrains.toolWindow

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import javax.swing.JLabel

class ErrorPanelDetailDialogWrapper(title: String, private val detail: String) : DialogWrapper(true) {
    init {
        setTitle(title)
        init()
    }

    override fun createCenterPanel() = JBScrollPane(JLabel("<html>${detail.replace("\n", "<br/>")}</html>"))
}

fun errorPanel(message: String, detail: String): JBPanelWithEmptyText {
    val panel = JBPanelWithEmptyText().withEmptyText(message)
    panel.emptyText.appendLine("Open details", SimpleTextAttributes.LINK_PLAIN_ATTRIBUTES) {
        ErrorPanelDetailDialogWrapper(message, detail).show()
    }

    return panel
}
