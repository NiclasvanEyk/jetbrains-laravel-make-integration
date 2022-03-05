package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBTextField
import javax.swing.event.DocumentEvent

fun JBTextField.onChange(listener: (newText: String) -> Unit): JBTextField {
    document.addDocumentListener(object: DocumentAdapter() {
        override fun textChanged(e: DocumentEvent) {
            listener(text)
        }
    })

    return this
}