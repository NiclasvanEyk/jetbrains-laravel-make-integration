package com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow.list

import com.intellij.ui.ListSpeedSearch
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.IntrospectionList
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.IntrospectionSubject
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.Command
import javax.swing.ListSelectionModel

class CommandList(
    commandUpdates: IntrospectionSubject<List<Command>>,
    onCommandSelected: (command: Command?) -> Unit,
) : IntrospectionList<Command>(commandUpdates) {
    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        selectionModel.addListSelectionListener {
            onCommandSelected(this.selectedValue)
        }

        cellRenderer = CommandListCellRenderer()

        ListSpeedSearch(this) { "${it.name} ${it.description}" }
        subscribeToModelUpdates()
    }

    override fun listKey(element: Command) = element.name
}
