package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.intellij.openapi.components.service

class RoutesLaravelApplicationListener: LaravelApplicationListener {
    override fun initialized(application: LaravelApplication) {
        val routeService = application.project.service<LaravelMakeRouteProjectService>()
        val introspecter = application.introspection.routeIntrospecter

        routeService.initialize(introspecter)
    }

    override fun event(name: String) {}
}