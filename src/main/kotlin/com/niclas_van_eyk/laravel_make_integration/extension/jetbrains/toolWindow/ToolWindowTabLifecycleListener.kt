package com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.toolWindow

import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener

interface ReceivesToolWindowTabLifecycleEvents {
    fun onTabFocused()
}

class ToolWindowTabLifecycleListener : ContentManagerListener {
    override fun selectionChanged(event: ContentManagerEvent) {
        if (event.operation !== ContentManagerEvent.ContentOperation.add) {
            return
        }

        val component = event.content.component

        if (component is ReceivesToolWindowTabLifecycleEvents) {
            component.onTabFocused()
        }
    }
}
