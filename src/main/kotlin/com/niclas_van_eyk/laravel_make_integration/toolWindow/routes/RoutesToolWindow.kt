package com.niclas_van_eyk.laravel_make_integration.toolWindow.routes

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.openapi.project.Project
import com.intellij.ui.*
import com.niclas_van_eyk.laravel_make_integration.actions.make.MakeControllerAction
import com.niclas_van_eyk.laravel_make_integration.services.LaravelMakeIntegrationProjectService
import com.niclas_van_eyk.laravel_make_integration.toolWindow.MasterDetailToolWindow
import javax.swing.*

class RoutesToolWindow(
    projectService: LaravelMakeIntegrationProjectService,
    project: Project,
) {
    private val routeList = RouteList(
        if (projectService.hasRoutes) projectService.routes.routes
        else emptyList(),
        project
    )

    private val toolbar = ActionToolbarImpl("LaravelToolWindowRoutesTab", DefaultActionGroup().apply {
        add(object : AnAction("Refresh", "Refresh routes", AllIcons.Actions.Refresh) {
            override fun actionPerformed(e: AnActionEvent) {
                projectService.routes.load { routeList.refreshRoutes(it) }
            }
        })
        add(object : AnAction("View Options", "View options", AllIcons.Actions.Show) {
            override fun actionPerformed(e: AnActionEvent) {
                routeList.showMiddlewareParameters = !routeList.showMiddlewareParameters
            }
        })
        addSeparator()
        add(object : AnAction("Create Controller", "Create a new controller using php artisan make:controller", AllIcons.General.Add) {
            override fun actionPerformed(e: AnActionEvent) {
                MakeControllerAction().actionPerformed(e)
            }
        })
    }, false)

    private val detailView = RouteDetailView()

    val component = MasterDetailToolWindow(routeList, detailView).apply {
        toolbar = this@RoutesToolWindow.toolbar
    }
}