package com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains.actions.MakeCommandAction
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar.RefreshButtonAction
import io.reactivex.rxjava3.core.Observable

class CommandsToolbar(
    isRefreshing: Observable<Boolean>,
    refresh: (e: AnActionEvent) -> Unit,
) {
    val component = ActionToolbarImpl(
        "LaravelToolWindowCommandsTab",
        DefaultActionGroup().apply {
            add(
                RefreshButtonAction(
                    "Refresh",
                    "Refresh commands",
                    isRefreshing,
                    refresh
                )
            )
//            add(object : AnAction(
//                "View Options",
//                "View options",
//                AllIcons.Actions.Show
//            ) {
//                    override fun actionPerformed(e: AnActionEvent) {
////                routeList.showMiddlewareParameters = !routeList.showMiddlewareParameters
//                    }
//                })
            addSeparator()
            add(object : AnAction(
                "Create Command",
                "Create a new command using php artisan make:command",
                AllIcons.General.Add,
            ) {
                    override fun actionPerformed(e: AnActionEvent) {
                        MakeCommandAction().actionPerformed(e)
                    }
                })
        },
        false
    )
}
