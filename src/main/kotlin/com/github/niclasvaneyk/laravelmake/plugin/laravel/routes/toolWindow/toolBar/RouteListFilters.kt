package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.toolBar

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar.FilterActionGroup
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list.RouteList

class RouteListFilters(private val routeList: RouteList) : FilterActionGroup() {
    init {
        add(object : ToggleAction("Application Routes") {
            override fun isSelected(e: AnActionEvent): Boolean {
                return routeList.showApplicationRoutes
            }

            override fun setSelected(e: AnActionEvent, state: Boolean) {
                routeList.showApplicationRoutes = state
            }
        })

        add(object : ToggleAction("Vendor Routes") {
            override fun isSelected(e: AnActionEvent): Boolean {
                return routeList.showVendorRoutes
            }

            override fun setSelected(e: AnActionEvent, state: Boolean) {
                routeList.showVendorRoutes = state
            }
        })

        add(object : ToggleAction("Closure Routes") {
            override fun isSelected(e: AnActionEvent): Boolean {
                return routeList.showClosureRoutes
            }

            override fun setSelected(e: AnActionEvent, state: Boolean) {
                routeList.showClosureRoutes = state
            }
        })
    }
}
