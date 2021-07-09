package com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.services.LaravelMakeIntegrationProjectService
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.toolWindow.CommandsToolWindow
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.RoutesToolWindow

class LaravelToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow,
    ) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val projectService = project.getService(
            LaravelMakeIntegrationProjectService::class.java
        )
        val laravelProject = projectService.laravelProject ?: return

        // Refresh routes, as this is the first tab the user will see
        laravelProject.introspection.routeIntrospecter.refresh()

        listOf(
            // Until we find a nice way of showing information here,
            // this tab also gets disabled, as the users are likely
            // to care about their routes the most (assumption)
            // Pair("Application", ApplicationToolWindow(toolWindow, project)),

            Pair(
                "Routes",
                RoutesToolWindow(
                    laravelProject,
                    laravelProject.introspection.routeIntrospecter,
                ),
            ),
            Pair(
                "Commands",
                CommandsToolWindow(
                    laravelProject,
                    laravelProject.introspection.commandIntrospecter,
                ),
            ),

            // Events are not a priority for now
            // Pair("Events", EventsToolWindow(projectService, project).component),
        ).forEach { (tabLabel, content) ->
            toolWindow.contentManager.addContent(
                contentFactory.createContent(content, tabLabel, false)
            )
        }

        toolWindow.contentManager.addContentManagerListener(
            ToolWindowTabLifecycleListener()
        )
    }

    override fun isApplicable(project: Project): Boolean {
        return project
            .getService(LaravelMakeIntegrationProjectService::class.java)
            .isLaravelProject
    }
}
