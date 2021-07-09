package com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.toolWindow.list

import com.intellij.ui.ListSpeedSearch
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.IntrospectionList
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.IntrospectionSubject
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.introspection.Command

class CommandList(
    commandUpdates: IntrospectionSubject<List<Command>>,
) : IntrospectionList<Command>(commandUpdates) {
    init {
        cellRenderer = CommandListCellRenderer()
        ListSpeedSearch(this) { "${it.name} ${it.description}" }
        subscribeToModelUpdates()
    }
}
