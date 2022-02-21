package com.github.niclasvaneyk.laravelmake.plugin.laravel.events.toolWindow

import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService

class EventsToolWindow(
    projectService: LaravelMakeProjectService,
    project: Project,
) {
//    private val eventList = EventList(
//        if (projectService.hasEvents) projectService.events.eventsAndListeners
//        else emptyList()
//    )
//
//    private val toolbar = ActionToolbarImpl(
//        "LaravelToolWindowRoutesTab",
//        DefaultActionGroup().apply {
//            add(object : AnAction("Refresh", "Refresh events", AllIcons.Actions.Refresh) {
//                override fun actionPerformed(e: AnActionEvent) {
//                    projectService.events.load {
//                        eventList.refreshEvents(it)
//                    }
//                }
//            })
//            add(object : AnAction("View Options", "View options", AllIcons.Actions.Show) {
//                override fun actionPerformed(e: AnActionEvent) {
//                    // TODO
//                }
//            })
//            addSeparator()
//            add(object : AnAction(
//                "Create Event",
//                "Create a new event using php artisan make:event",
//                AllIcons.General.Add,
//            ) {
//                    override fun actionPerformed(e: AnActionEvent) {
//                        MakeEventAction().actionPerformed(e)
//                    }
//                })
//            add(object : AnAction(
//                "Create Listener",
//                "Create a new listener using php artisan make:listener",
//                AllIcons.General.Add,
//            ) {
//                    override fun actionPerformed(e: AnActionEvent) {
//                        MakeListenerAction().actionPerformed(e)
//                    }
//                })
//        },
//        false
//    )
//
//    private val detailView = JBPanelWithEmptyText().withEmptyText("Select an event/listener to see more information")
//
//    val component = MasterDetailToolWindow(eventList, detailView).apply {
//        toolbar = this@EventsToolWindow.toolbar
//    }
}
