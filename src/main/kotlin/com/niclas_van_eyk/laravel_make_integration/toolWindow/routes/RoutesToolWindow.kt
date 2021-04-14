package com.niclas_van_eyk.laravel_make_integration.toolWindow.routes

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.ToggleAction
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

    // TODO: look into how the TODO-ToolWindow displays
    //       the comments inline + how they do the spacing
    private val toolbar: RoutesToolbar = RoutesToolbar(
        routeList
    ) { onFinish: () -> Unit ->
        projectService.routes.load {
            routeList.refreshRoutes(it)
            // TODO: Also call this in the error case
            onFinish()
        }
    }

    private val detailView = RouteDetailView()

    val component = MasterDetailToolWindow(routeList, detailView.component).apply {
        toolbar = this@RoutesToolWindow.toolbar.component
    }
}