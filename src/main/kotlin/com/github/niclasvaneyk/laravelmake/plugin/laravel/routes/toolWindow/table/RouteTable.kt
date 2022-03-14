package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.table

import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.Column
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.IntrospectionTable
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.KJBTableModel
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.IntrospectionSubject
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.basename
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ClosureRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.intellij.openapi.project.Project

private val routeTableCells = KJBTableModel<IntrospectedRoute>(
    Column("Method", { it.httpMethod }),
    Column("Path", { it.path }),
    Column("Name", { it.name }),
    Column("Middleware", { it.middleware.map { m -> m.basename }.filter{ m -> m.isNotBlank() }.joinToString { ", " } }),
    Column("FormRequest", {
        if (it is ControllerRoute && it.formRequest != null) basename(it.formRequest.fqn)
        else null
    }),
    Column( "Action", {
        if (it is ControllerRoute) "${basename(it.`class`.fqn)}@${it.method.name}"
        else null
    })
)

class RouteTable(
    routeUpdates: IntrospectionSubject<List<IntrospectedRoute>>,
    private val project: Project,
): IntrospectionTable<IntrospectedRoute>(routeTableCells, routeUpdates) {
    init {
        fillsViewportHeight = true
        subscribeToModelUpdates()
    }

    override fun listKey(element: IntrospectedRoute): String {
        return element.httpMethod + " " + element.path
    }
}