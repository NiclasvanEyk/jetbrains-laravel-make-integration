package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import javax.swing.JTextArea

class ErrorPanelDetailDialogWrapper(title: String, private val detail: String) : DialogWrapper(true) {
    init {
        setTitle(title)
        init()
    }

    override fun createCenterPanel(): JBScrollPane {
        return JBScrollPane(JTextArea(detail).apply {
            isEditable = false
        })
    }
}

fun errorPanel(message: String, detail: String): JBPanelWithEmptyText {
    val panel = JBPanelWithEmptyText().withEmptyText(message)
    panel.emptyText.appendLine("Open details", SimpleTextAttributes.LINK_PLAIN_ATTRIBUTES) {
        ErrorPanelDetailDialogWrapper(message, detail).show()
    }

    return panel
}
