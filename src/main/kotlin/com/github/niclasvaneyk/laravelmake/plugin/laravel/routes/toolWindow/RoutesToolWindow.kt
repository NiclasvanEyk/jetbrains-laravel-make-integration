package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.*
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow.errorPanel
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelProject
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteIntrospecter
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list.RouteList
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.toolBar.RoutesToolbar
import java.awt.Graphics
import java.awt.event.ComponentListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.toolWindow.IntrospectionBasedToolWindowRevalidator as Revalidator

class RoutesToolWindow(
    project: LaravelProject,
    private val introspecter: RouteIntrospecter = project.introspection.routeIntrospecter,
    private val revalidator: Revalidator<List<IntrospectedRoute>> = Revalidator(
        introspecter
    )
):
    SimpleToolWindowPanel(false),
    ReceivesToolWindowTabLifecycleEvents by revalidator
{
    private val documentation = RouteDocumentation(project.jetbrainsProject)

    private var selectedRoute: IntrospectedRoute? = null
        set(value) {
            field = value
//            if (value != null) {
//                documentation.showPreview(value)
//            } else {
//                documentation.showMessage()
//            }
        }

    private val routeList: RouteList = RouteList(
        routeUpdates = introspecter.introspectionState,
        project = project.jetbrainsProject,
        onRouteSelected = { selectedRoute = it }
    )

    private val masterListPanel = JBScrollPane(routeList).apply {
        border = null // SideBorder(JBColor.border(), SideBorder.LEFT)
    }

    init {
        toolbar = RoutesToolbar(
            routeList,
            introspecter = project.introspection.routeIntrospecter,
        )

        setContent(masterListPanel)
//        detail = documentation

        project.introspection.routes.subscribe(
            { onStateUpdate(it) },
            { onError(it.toString() + "\n" + it.stackTraceToString()) }
        )
    }

    private fun onStateUpdate(it: IntrospectionState<List<IntrospectedRoute>>) {
        when (it) {
            is InitialState -> { /* Not really important to us here */ }

            is InitialLoadingState -> {
                setContent(JBPanelWithEmptyText().withEmptyText("Loading..."))
                documentation.showMessage("Loading...")
            }

            is LoadedState -> {
                setContent(masterListPanel)
                documentation.showMessage()
            }

            is RevalidatingState -> { /* Not really important to us here */ }

            is RevalidatedState -> {
                setContent(masterListPanel)
                documentation.showMessage()
            }

            is ErrorState -> onError(it.message)
        }
    }

    private fun onError(details: String = "") {
        setContent(errorPanel("An error occurred while loading routes", details))
        documentation.showMessage("")
    }
}
