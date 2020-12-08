package com.niclas_van_eyk.laravel_make_integration.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.niclas_van_eyk.laravel_make_integration.services.LaravelMakeIntegrationProjectService

internal class MyProjectManagerListener : ProjectManagerListener {
    override fun projectOpened(project: Project) {
    }
}
