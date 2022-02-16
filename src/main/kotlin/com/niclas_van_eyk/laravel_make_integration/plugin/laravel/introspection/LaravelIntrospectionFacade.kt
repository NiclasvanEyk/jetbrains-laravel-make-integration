package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.introspection

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.common.laravel.Artisan
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.introspection.CommandIntrospecter
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteIntrospecter

class LaravelIntrospectionFacade(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    project: Project,
) {
    val routeIntrospecter = RouteIntrospecter(artisan, progressBar, project)
    val routes get() = routeIntrospecter.introspectionState

    val commandIntrospecter = CommandIntrospecter(artisan, progressBar)
    val commands get() = commandIntrospecter.introspectionState
}
