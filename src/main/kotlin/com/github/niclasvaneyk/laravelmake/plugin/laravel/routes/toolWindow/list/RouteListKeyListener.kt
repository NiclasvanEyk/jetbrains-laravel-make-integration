package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ClosureRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.intellij.openapi.project.Project
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class RouteListKeyListener(
    private val list: RouteList,
    private val project: Project
) : KeyListener {
    override fun keyTyped(e: KeyEvent?) { }
    override fun keyPressed(e: KeyEvent?) { }

    override fun keyReleased(e: KeyEvent?) {
        if (e?.keyCode != KeyEvent.VK_ENTER) {
            return
        }

        val selectedRoute = list.selectedValue ?: return
        if (selectedRoute is ControllerRoute) {
            selectedRoute.navigate()
        } else if (selectedRoute is ClosureRoute) {
            selectedRoute.navigate(project)
        }
    }
}