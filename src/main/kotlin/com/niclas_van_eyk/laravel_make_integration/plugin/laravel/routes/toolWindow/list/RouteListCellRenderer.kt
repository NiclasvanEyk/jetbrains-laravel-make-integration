package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.Colors
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.ControllerMethodAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.InvocableControllerAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteListEntry
import javax.swing.Icon
import javax.swing.JList

class MiddlewareRenderer(
    private val showParams: Boolean,
    private val fullyQualifyNames: Boolean,
    middleware: String,
) {
    private val fullyQualifiedName = middleware.split(":")[0]
    private val parameters = middleware.split(":", limit = 2).getOrNull(1)
    private val displayName: String get() =
        if (fullyQualifyNames) fullyQualifiedName
        else fullyQualifiedName.substringAfterLast("\\")

    override fun toString() =
        if (showParams && parameters != null) "$displayName:$parameters"
        else displayName
}

class RouteListCellRenderer(
    private val showMiddlewareParameters: Boolean,
    private val fullyQualifyMiddlewareNames: Boolean,
    private val project: Project,
) : ColoredListCellRenderer<RouteListEntry>() {
    override fun customizeCellRenderer(
        list: JList<out RouteListEntry>,
        value: RouteListEntry?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ) {
        if (value == null) {
            return
        }

        icon = iconFor(value.controllerAction)

        if (value.isVendorRoute(project) && value !== list.selectedValue ) {
            background = Colors.vendor(project)
        }

        append(
            // I personally think it's nicer if *all* routes start with a /
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
        val middleware = value.middleware.map {
            MiddlewareRenderer(showMiddlewareParameters, fullyQualifyMiddlewareNames, it).toString()
        }

        append(
            middleware.joinToString(" "),
            SimpleTextAttributes.GRAYED_ATTRIBUTES
        )
    }

    private fun iconFor(action: RouteAction): Icon = when (action) {
        is ControllerMethodAction -> AllIcons.Nodes.Method
        is InvocableControllerAction -> AllIcons.Nodes.Class
        else /* is Closure */ -> AllIcons.Nodes.Field
        // As the function icon has the same background color as the class
        // one, we use field here, as it is also just a circled f, but in
        // a different color.
    }
}
