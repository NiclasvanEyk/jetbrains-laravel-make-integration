package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.toolBar

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolBar.ViewOptionsActionGroup
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list.RouteList

class RouteListViewOptionsSelector(
    private val routeList: RouteList
): ViewOptionsActionGroup() {
    init {
        add(object : ToggleAction("Show Middleware Parameters") {
            override fun isSelected(e: AnActionEvent): Boolean {
                return routeList.showMiddlewareParameters
            }

            override fun setSelected(e: AnActionEvent, state: Boolean) {
                routeList.showMiddlewareParameters = state
            }
        })
        add(object : ToggleAction("Show Middleware FQN") {
            override fun isSelected(e: AnActionEvent): Boolean {
                return routeList.fullyQualifyMiddlewareNames
            }

            override fun setSelected(e: AnActionEvent, state: Boolean) {
                routeList.fullyQualifyMiddlewareNames = state
            }
        })
    }
}
