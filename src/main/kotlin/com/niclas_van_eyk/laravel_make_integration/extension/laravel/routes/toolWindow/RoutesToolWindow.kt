package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow

import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolWindow.MasterDetailToolWindow
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.ErrorState
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.InitialLoadingState
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteIntrospecter
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.introspection.toolWindow.IntrospectionBasedToolWindowRevalidator as Revalidator
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteListEntry
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.list.RouteList
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.toolBar.RoutesToolbar


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
    private val routeList: RouteList = RouteList(
        routeUpdates = introspecter.introspectionState,
        project = project.jetbrainsProject,
        onRouteSelected = {
            detail = RouteDocumentation.forRoute(
                it?.controllerAction,
                project.jetbrainsProject,
            )
        }
    )

    init {
        toolbar = RoutesToolbar(
            routeList,
            introspecter = project.introspection.routeIntrospecter,
        )

        master = JBScrollPane(routeList).apply {
            border = SideBorder(JBColor.border(), SideBorder.LEFT)
        }
        detail = RouteDocumentation.empty()

        project.introspection.routes.subscribe {
            if (it is InitialLoadingState) {
                detail = JBPanelWithEmptyText().withEmptyText("Loading...")
            }

            if (it is ErrorState) {
                detail = JBPanelWithEmptyText()
            }
        }
    }
}
