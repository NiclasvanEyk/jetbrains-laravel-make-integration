package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteListEntry
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.SwingUtilities

@Suppress("EmptyFunctionBlock")
class RouteListMouseListener(
    private val list: RouteList,
    private val project: Project,
    private val onRouteSelected: (RouteListEntry?) -> Unit,
) : MouseListener {
    var previouslySelectedRoute: RouteListEntry? = null

    override fun mousePressed(e: MouseEvent?) {
        val point = e?.point ?: return

        if (SwingUtilities.isRightMouseButton(e)) {
            val clicked = list.model.getElementAt(list.locationToIndex(point))
            RouteListContextMenu(clicked, project).show(list, e.x, e.y)
        }

        if (e.clickCount >= 2) {
            list.selectedValue.jumpToControllerActionSource(project)
        }
    }

    // We don't need these
    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
}
