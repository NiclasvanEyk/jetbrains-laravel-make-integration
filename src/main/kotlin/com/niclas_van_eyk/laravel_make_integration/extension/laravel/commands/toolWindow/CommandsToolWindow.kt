package com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.toolWindow

import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolWindow.MasterDetailToolWindow
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.introspection.Command
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.introspection.CommandIntrospecter
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.toolWindow.list.CommandList
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.introspection.toolWindow.IntrospectionBasedToolWindowRevalidator as Revalidator

class CommandsToolWindow(
    project: LaravelProject,
    private val introspecter: CommandIntrospecter,
    private val revalidator: Revalidator<List<Command>> = Revalidator(introspecter),
) :
    MasterDetailToolWindow(),
    ReceivesToolWindowTabLifecycleEvents by revalidator
{
    private var commandList: CommandList = CommandList(
        commandUpdates = project.introspection.commands,
        onCommandSelected = { detail = CommandDocumentation.forCommand(it) }
    )

    init {
        master = JBScrollPane(commandList).apply {
            border = SideBorder(JBColor.border(), SideBorder.LEFT)
        }
        detail = CommandDocumentation.empty()

        toolbar = CommandsToolbar(
            isRefreshing = project.introspection.commands.map { it.loading },
            refresh = { project.introspection.commandIntrospecter.refresh() },
        ).component
    }
}
