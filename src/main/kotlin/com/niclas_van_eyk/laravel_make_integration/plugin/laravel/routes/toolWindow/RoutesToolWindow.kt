package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow

import com.intellij.diagnostic.LoadingState
import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolWindow.MasterDetailToolWindow
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.*
import com.niclas_van_eyk.laravel_make_integration.plugin.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteIntrospecter
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.introspection.toolWindow.IntrospectionBasedToolWindowRevalidator as Revalidator
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteListEntry
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list.RouteList
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.toolBar.RoutesToolbar


class RoutesToolWindow(
    private val project: LaravelProject,
    private val introspecter: RouteIntrospecter,
    private val revalidator: Revalidator<List<RouteListEntry>> = Revalidator(
        introspecter
    )
):
    MasterDetailToolWindow(),
    ReceivesToolWindowTabLifecycleEvents by revalidator
{
    private val documentation = RouteDocumentation(project.jetbrainsProject)

    private var selectedRoute: RouteListEntry? = null
        set(value) {
            field = value
            val route = value?.controllerAction
            if (route != null) {
                documentation.showPreview(route)
            } else {
                documentation.showMessage()
            }
        }

    private val routeList: RouteList = RouteList(
        routeUpdates = introspecter.introspectionState,
        project = project.jetbrainsProject,
        onRouteSelected = { selectedRoute = it }
    )

    private val masterListPanel = JBScrollPane(routeList).apply {
        border = SideBorder(JBColor.border(), SideBorder.LEFT)
    }

    init {
        toolbar = RoutesToolbar(
            routeList,
            introspecter = project.introspection.routeIntrospecter,
        )

        master = masterListPanel
        detail = documentation

        project.introspection.routes.subscribe(
            { onStateUpdate(it) },
            { onError() }
        )
    }

    private fun onStateUpdate(it: IntrospectionState<List<RouteListEntry>>) {
        when (it) {
            is InitialState -> { /* Not really important to us here */ }

            is InitialLoadingState -> {
                master = JBPanelWithEmptyText().withEmptyText("Loading...")
                documentation.showMessage("Loading...")
            }

            is LoadedState -> {
                if (master !is RouteList) {
                    master = masterListPanel
                    documentation.showMessage()
                }
            }

            is RevalidatingState -> { /* Not really important to us here */ }

            is RevalidatedState -> {
                if (master !is RouteList) {
                    master = masterListPanel
                    documentation.showMessage()
                }
            }

            is ErrorState -> onError()
        }
    }

    private fun onError(message: String = "An error occurred while loading routes") {
        master = JBPanelWithEmptyText().withEmptyText(message)
        documentation.showMessage(message)
    }
}
