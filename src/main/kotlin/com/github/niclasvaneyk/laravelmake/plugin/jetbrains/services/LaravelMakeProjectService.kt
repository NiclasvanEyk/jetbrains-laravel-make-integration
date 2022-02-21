package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationFactory
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project

@Service(Level.PROJECT)
class LaravelMakeProjectService(val project: Project) {
    val application = LaravelApplicationFactory(project).build()
    val isLaravelProject: Boolean get() = application != null

    init {
        if (application == null) {
            val log = logger<LaravelMakeProjectService>()
            log.info("${project.name} is not Laravel project")
        } else {
            application.initialize()
        }
    }
}
