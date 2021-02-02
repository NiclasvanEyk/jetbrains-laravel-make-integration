package com.niclas_van_eyk.laravel_make_integration.services

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.LaravelMakeIntegrationBundle
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProjectFactory
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.ProjectCommands
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.ProjectConfig
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.ProjectRoutes

class LaravelMakeIntegrationProjectService(project: Project) {
    private val projectFactory = LaravelProjectFactory(project.basePath!!)
    val laravelProject = projectFactory.build()
    val isLaravelProject: Boolean
        get() = laravelProject != null

    lateinit var commands: ProjectCommands
    val hasCommands: Boolean get() = commands.commands.isNotEmpty()

    lateinit var routes: ProjectRoutes
    val hasRoutes: Boolean get() = routes.routes.isNotEmpty()

    lateinit var config: ProjectConfig
    val hasConfig: Boolean get() = config.config !== null

    init {
        if (!isLaravelProject) {
            println(LaravelMakeIntegrationBundle.message(
                    "projectService",
                    "${project.name} is not a valid Laravel project: "
                            + projectFactory.errors.joinToString(", ")
            ))
        } else {
            laravelProject!!

            commands = ProjectCommands(laravelProject, project)
            commands.load {}

            routes = ProjectRoutes(laravelProject, project)
            routes.load()

            config = ProjectConfig(laravelProject, project)
            config.load()
        }
    }
}
