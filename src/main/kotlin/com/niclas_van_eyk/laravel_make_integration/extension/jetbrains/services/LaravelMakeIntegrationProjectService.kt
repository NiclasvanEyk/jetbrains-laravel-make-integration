package com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.LaravelMakeIntegrationBundle
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.LaravelProjectFactory
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.php.PHPInterpreterValidator

@Service
class LaravelMakeIntegrationProjectService(project: Project) {
    private val projectFactory = LaravelProjectFactory(project)
    val laravelProject = projectFactory.build()
    val isLaravelProject: Boolean
        get() = laravelProject != null

    init {
        if (!isLaravelProject) {
            println(
                LaravelMakeIntegrationBundle.message(
                    "projectService",
                    "${project.name} is not a valid Laravel project: " +
                        projectFactory.errors.joinToString(", ")
                )
            )
        } else {
            laravelProject!!

            ProgressBarBuilder(project).indeterminate("Validate Project Interpreter") {
                val validator = PHPInterpreterValidator(laravelProject)
                val result = validator.validate()

                // TODO: Maybe we should have this functionality be triggered by an
                //       action?
                if (!result.isValid) {
                    result.showNotification()
                }
            }
        }
    }
}
