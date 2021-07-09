package com.niclas_van_eyk.laravel_make_integration.extension.laravel.introspection.toolWindow

import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandBasedIntrospecter
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.InitialState
import com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.toolWindow.ReceivesToolWindowTabLifecycleEvents

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
