package com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection

import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.CommandIntrospecter
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteIntrospecter

class LaravelIntrospectionFacade(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    project: Project,
) {
    val routeIntrospecter = RouteIntrospecter(artisan, progressBar, project)
    val routes get() = routeIntrospecter.introspectionState

    val commandIntrospecter = CommandIntrospecter(artisan, progressBar)
    val commands get() = commandIntrospecter.introspectionState

    fun refresh() {
        routeIntrospecter.refresh()
        commandIntrospecter.refresh()
    }
}
