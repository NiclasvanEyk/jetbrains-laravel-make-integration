package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.Colors
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.*
import javax.swing.Icon
import javax.swing.JList

private class MiddlewareRenderer(
    private val showParams: Boolean,
    private val fullyQualifyNames: Boolean,
    private val middleware: IntrospectedMiddleware,
) {
    private val parameters = middleware.parameters.joinToString(",")
    private val displayName: String get() =
        if (fullyQualifyNames) middleware.name
        else middleware.name.substringAfterLast("\\")

    override fun toString() =
        if (showParams && middleware.parameters.isNotEmpty()) "$displayName:$parameters"
        else displayName
}

private fun routeListIcon(action: IntrospectedRoute): Icon = when (action) {
    // As the function icon has the same background color as the class
    // one, we use field here, as it is also just a circled f, but in
    // a different color.
    is ClosureRoute -> AllIcons.Nodes.Field
    is ControllerRoute -> if (action.isInvokableControllerRoute)
        AllIcons.Nodes.Class else
        AllIcons.Nodes.Method
}

class RouteListCellRenderer(
    private val showMiddlewareParameters: Boolean,
    private val fullyQualifyMiddlewareNames: Boolean,
    private val project: Project,
) : ColoredListCellRenderer<IntrospectedRoute>() {
    override fun customizeCellRenderer(
        list: JList<out IntrospectedRoute>,
        value: IntrospectedRoute?,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ) {
        if (value == null) {
            return
        }

        icon = routeListIcon(value)

        if (value.origin === RouteOrigin.VENDOR && value !== list.selectedValue ) {
            background = Colors.vendor(project)
        }

        append(
            value.path,
            SimpleTextAttributes.REGULAR_ATTRIBUTES,
            true
        )

        append(" ")
        append(
            // I do not see the value of HEAD, as it is most likely of no use
            value.httpMethod.replace("GET|HEAD", "GET"),
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
}
