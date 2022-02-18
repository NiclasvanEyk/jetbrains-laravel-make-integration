package com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow.list

import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.Command
import javax.swing.JList
import javax.swing.SwingConstants

class CommandListCellRenderer: ColoredListCellRenderer<Command>() {
    override fun customizeCellRenderer(
        list: JList<out Command>,
        value: Command?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ) {
        if (value != null) {
            append(value.name, SimpleTextAttributes.REGULAR_ATTRIBUTES, true)
            append(" ")
            append(value.description, SimpleTextAttributes.GRAYED_ATTRIBUTES, SwingConstants.RIGHT)
        }
    }
}
