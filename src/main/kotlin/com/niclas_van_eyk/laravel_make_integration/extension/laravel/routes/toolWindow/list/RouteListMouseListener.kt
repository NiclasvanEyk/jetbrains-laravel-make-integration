package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.list

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteListEntry
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.SwingUtilities

@Suppress("EmptyFunctionBlock")
class RouteListMouseListener constructor(
    private val list: RouteList,
    private val project: Project,
    private val onRouteSelected: (RouteListEntry?) -> Unit,
) : MouseListener {
    var previouslySelectedRoute: RouteListEntry? = null

    override fun mousePressed(e: MouseEvent?) {
        val point = e?.point ?: return


        list.selectedIndex = list.locationToIndex(point)
        val selected = list.selectedValue

        if (selected !== previouslySelectedRoute) {
            onRouteSelected(selected)
            previouslySelectedRoute = selected
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            RouteListContextMenu(selected, project).show(list, e.x, e.y)
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
