package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.toolWindow.list

import com.intellij.ui.ListSpeedSearch
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.IntrospectionList
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.IntrospectionSubject
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.introspection.Command
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
