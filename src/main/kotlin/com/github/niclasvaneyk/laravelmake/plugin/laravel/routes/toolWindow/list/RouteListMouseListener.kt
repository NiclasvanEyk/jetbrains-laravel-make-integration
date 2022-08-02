package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.SwingUtilities

@Suppress("EmptyFunctionBlock")
class RouteListMouseListener(private val list: RouteList) : MouseListener {
    override fun mousePressed(e: MouseEvent?) {
        val point = e?.point ?: return

        if (SwingUtilities.isRightMouseButton(e)) {
            val clicked = list.model.getElementAt(list.locationToIndex(point))
            RouteListContextMenu(clicked).show(list, e.x, e.y)
        }

        if (e.clickCount >= 2) {
            val selectedRoute = list.selectedValue
            if (selectedRoute is ControllerRoute) {
                selectedRoute.navigate()
            }
        }
    }

    // We don't need these
    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
}
