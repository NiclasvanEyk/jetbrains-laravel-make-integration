package com.github.niclasvaneyk.laravelmake.plugin.laravel.introspection.toolWindow

import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.InitialState
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents

class IntrospectionBasedToolWindowRevalidator<T>(
    private val introspecter: CommandBasedIntrospecter<T>
) : ReceivesToolWindowTabLifecycleEvents {
    override fun onTabFocused() {
        val currentState = introspecter.introspectionStateSource.value
        val hasNotBeenLoadedYet = currentState is InitialState
        val isStale = introspecter.staleDataDetector.isStale
        val isLoading = currentState.loading

        if (!isLoading && (hasNotBeenLoadedYet || isStale)) {
            introspecter.refresh()
        }
    }
}
