package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.toolBar

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolBar.IntrospecterRefreshButtonAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteIntrospecter
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.list.RouteList

class RoutesToolbar(
    private val routeList: RouteList,
    introspecter: RouteIntrospecter,
): ActionToolbarImpl(
    "LaravelToolWindowRoutesTab",
    DefaultActionGroup().apply {
        add(IntrospecterRefreshButtonAction(introspecter, "Refresh routes"))
        add(RouteListViewOptionsSelector(routeList))
        add(RouteListFilters(routeList))

        addSeparator()

        add(CreateControllerAction())
    },
    false
)
