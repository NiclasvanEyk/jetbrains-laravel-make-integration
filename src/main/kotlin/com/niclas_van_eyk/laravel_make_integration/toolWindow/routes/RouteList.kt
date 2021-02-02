package com.niclas_van_eyk.laravel_make_integration.toolWindow.routes

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.ListSpeedSearch
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBList
import com.jetbrains.php.PhpIndex
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*

class RouteList(
    private val routes: List<RouteListEntry>,
    private val project: Project
): JBList<RouteListEntry>(routes) {
    var showMiddlewareParameters = false
    set(value) {
        field = value
        triggerRender()
    }

    init {
        addMouseListener(object: MouseListener {
            override fun mousePressed(e: MouseEvent?) {
                if (e != null && SwingUtilities.isRightMouseButton(e)) {
                    selectedIndex = locationToIndex(e.point)
                    val selected = this@RouteList.selectedValue

                    JBPopupMenu().apply {
                        add(JMenuItem("Jump To Source", AllIcons.Actions.EditSource).apply {
                            // Needs to be an invocable controller or a controller method
                            isEnabled = canJumpToControllerActionSource(selectedValue)
                            addActionListener { jumpToControllerActionSource(selected) }
                        })
                    }.show(this@RouteList, e.x, e.y)
                }

                if (e != null && e.clickCount >= 2) {
                    jumpToControllerActionSource(selectedValue)
                }
            }
            override fun mouseClicked(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}
        })

        selectionMode = ListSelectionModel.SINGLE_SELECTION

        cellRenderer = object: ColoredListCellRenderer<RouteListEntry>() {
            override fun customizeCellRenderer(list: JList<out RouteListEntry>, value: RouteListEntry?, index: Int, selected: Boolean, hasFocus: Boolean) {
                if (value != null) {
                    icon = iconFor(value.controllerAction)

                    append(
                        // I personally think its nicer if *all* routes start with a /
                        if (value.uri.startsWith("/")) value.uri else "/${value.uri}",
                        SimpleTextAttributes.REGULAR_ATTRIBUTES,
                        true
                    )

                    append(" ")
                    append(
                        // I do not see the value of HEAD, as it is most likely of no use
                        value.method.replace("GET|HEAD", "GET"),
                        SimpleTextAttributes.GRAYED_ATTRIBUTES,
                        false
                    )

                    append(" ")
                    var middleware = value.middleware

                    // Sometimes the middleware params are only noise, so we hide them by default. Especially since they
                    // are displayed like `web, guest, throttle:1,2,3`, so the added commas are a bit confusing (they
                    // make it harder to differentiate if a new middleware starts or a new parameter).
                    if (!this@RouteList.showMiddlewareParameters) {
                        middleware = middleware.map {
                            if (it.contains(":")) it.substring(0, it.indexOf(":"))
                            else it
                        }
                    }

                    append(
                        middleware.joinToString(", "),
                        SimpleTextAttributes.GRAYED_ATTRIBUTES
                    )
                }
            }
        }

        ListSpeedSearch(this) { it.uri }
    }

    fun refreshRoutes(routes: List<RouteListEntry>) {
        (model as DefaultListModel).apply {
            removeAllElements()
            addAll(routes)
        }
    }

    fun triggerRender() = this.refreshRoutes(routes)

    fun canJumpToControllerActionSource(route: RouteListEntry) = route.controllerAction is ClassBasedRouteAction

    fun iconFor(action: RouteAction): Icon = when (action) {
        is ControllerMethodAction -> AllIcons.Nodes.Method
        is InvocableControllerAction -> AllIcons.Nodes.Class
        else /* is Closure */ -> AllIcons.Nodes.Field // As the function icon has the same background color as the class
                                                      // one, we use field here, as it is also just a circled f, but in
                                                      // a different color.
    }

    fun jumpToControllerActionSource(route: RouteListEntry) {
        val action = route.controllerAction

        if (action !is ClassBasedRouteAction) return

        val clazz = PhpIndex.getInstance(project).getClassesByFQN(action.className).first() ?: return

        if (action is ControllerMethodAction) {
            clazz.findMethodByName(action.methodName)?.navigate(true)
        } else {
            clazz.navigate(true)
        }
    }
}