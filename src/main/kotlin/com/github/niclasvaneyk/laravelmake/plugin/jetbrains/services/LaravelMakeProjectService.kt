package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationFactory
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.project.Project

@Service(Level.PROJECT)
class LaravelMakeProjectService(val project: Project) {
    val application = LaravelApplicationFactory(project).build()
    val isLaravelProject: Boolean get() = application != null
}
