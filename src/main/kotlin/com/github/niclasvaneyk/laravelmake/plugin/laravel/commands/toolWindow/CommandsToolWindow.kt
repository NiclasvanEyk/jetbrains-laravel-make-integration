package com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow

import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBScrollPane
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolWindow.MasterDetailToolWindow
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.Command
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.CommandIntrospecter
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow.list.CommandList
import com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.toolWindow.IntrospectionBasedToolWindowRevalidator as Revalidator

class CommandsToolWindow(
    project: LaravelApplication,
    private val introspecter: CommandIntrospecter = project.introspection.commandIntrospecter,
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
