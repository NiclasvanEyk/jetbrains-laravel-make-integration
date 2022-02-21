package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class RouteListKeyListener(private val list: RouteList) : KeyListener {
    override fun keyTyped(e: KeyEvent?) { }
    override fun keyPressed(e: KeyEvent?) { }

    override fun keyReleased(e: KeyEvent?) {
        if (e?.keyCode != KeyEvent.VK_ENTER) {
            return
        }

        val selectedRoute = list.selectedValue ?: return
        if (selectedRoute is ControllerRoute) {
            selectedRoute.method.navigate(false)
        }
    }
}