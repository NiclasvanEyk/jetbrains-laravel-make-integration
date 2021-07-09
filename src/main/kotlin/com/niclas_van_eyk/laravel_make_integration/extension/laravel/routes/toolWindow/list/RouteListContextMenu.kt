package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.list

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBPopupMenu
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteListEntry
import javax.swing.JMenuItem

class RouteListContextMenu(
    private val route: RouteListEntry,
    project: Project,
) : JBPopupMenu() {
    init {
        add(
            JMenuItem("Jump To Source", AllIcons.Actions.EditSource).apply {
                // Needs to be an invocable controller or a controller method
                isEnabled = route.canJumpToControllerActionSource()
                addActionListener {
                    route.jumpToControllerActionSource(project)
                }
            }
        )

        val formRequestClass = route.formRequestClass(project)

        if (formRequestClass != null) {
            add(
                JMenuItem(
                    "Jump To Request",
                    AllIcons.Actions.EditSource,
                ).apply {
                    addActionListener {
                        formRequestClass.navigate(true)
                    }
                }
            )
        }
    }
}
