package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.toolBar

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar.IntrospecterRefreshButtonAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteIntrospecter
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list.RouteList

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
) {
    init {
        targetComponent = component
    }
}
