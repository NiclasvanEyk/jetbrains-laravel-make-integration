package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.php.run.NoInterpreterSetException
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelProjectFactory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.PHPInterpreterValidator

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
