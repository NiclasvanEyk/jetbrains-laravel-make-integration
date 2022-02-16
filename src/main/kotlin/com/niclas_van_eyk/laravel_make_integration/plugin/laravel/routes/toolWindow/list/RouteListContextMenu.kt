package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBPopupMenu
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.*
import javax.swing.JMenuItem

class RouteListContextMenu(private val route: IntrospectedRoute) : JBPopupMenu() {
    init {
        add(
            JMenuItem("Jump To Source", AllIcons.Actions.EditSource).apply {
                isEnabled = route is ControllerRoute
                addActionListener {
                    if (route is ControllerRoute ) {
                        route.method.navigate(true)
                    }
                }
            }
        )

        val formRequestClass = if (route is ControllerRoute) route.formRequest else null
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
