package com.niclas_van_eyk.laravel_make_integration.services

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.LaravelMakeIntegrationBundle
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProjectFactory
import java.nio.file.Paths

class MyProjectService(project: Project) {
    private val projectFactory = LaravelProjectFactory(project.basePath!!)
    val laravelProject = projectFactory.build()
    val isLaravelProject: Boolean
        get() = laravelProject != null

    init {
        if (!isLaravelProject) {
            println(LaravelMakeIntegrationBundle.message(
                    "projectService",
                    "${project.name} is not a valid Laravel project: "
                            + projectFactory.errors.joinToString(", ")
            ))
        }
    }
}
