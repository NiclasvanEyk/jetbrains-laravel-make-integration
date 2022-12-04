package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.toolWindow.CommandsToolWindow
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.RoutesToolWindow
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ex.ToolWindowManagerListener

class MyListener: ToolWindowManagerListener {
    override fun toolWindowShown(toolWindow: ToolWindow) {
        super.toolWindowShown(toolWindow)

        logger<MyListener>().warn("Shown: ${toolWindow.id}");
    }
}

class LaravelToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow,
    ) {
        val contentFactory = ContentFactory.getInstance()
        val projectService = project.getService(
            LaravelMakeProjectService::class.java
        )
        val laravelProject = projectService.application ?: return

        listOf(
            // Until we find a nice way of showing information here,
            // this tab also gets disabled, as the users are likely
            // to care about their routes the most (assumption)
            // Pair("Application", ApplicationToolWindow(toolWindow, project)),
            Pair("Routes", RoutesToolWindow(laravelProject)),
            Pair("Commands", CommandsToolWindow(laravelProject)),

            // Events are not a priority for now
            // Pair("Events", EventsToolWindow(projectService, project).component),
        ).forEach { (tabLabel, content) ->
            val tabContent = contentFactory.createContent(content, tabLabel, false).apply {
                addPropertyChangeListener {
                    logger<LaravelToolWindowFactory>().warn(it.propertyName)
                }
            }

            toolWindow.activate {
                logger<LaravelToolWindowFactory>().warn("ACTIVATED!")
            }

            toolWindow.contentManager.addContent(tabContent)
        }

        toolWindow.contentManager.addContentManagerListener(
            ToolWindowTabLifecycleListener()
        )

        // Refresh routes, as this is the first tab the user will see
        laravelProject.introspection.routeIntrospecter.refresh()
    }

    override fun isApplicable(project: Project): Boolean {
        return project.service<LaravelMakeProjectService>() .application != null
    }
}
