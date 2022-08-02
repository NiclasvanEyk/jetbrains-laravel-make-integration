package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list

import com.github.niclasvaneyk.laravelmake.common.laravel.Livewire
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute

/**
 * Handles the navigation from an entry in the route list.
 */
fun ControllerRoute.navigate(requestFocus: Boolean = true) {
    if (isHandledByLivewire) {
        navigateToLivewireComponent(requestFocus)
        return
    }

    method.navigate(requestFocus)
}

private val ControllerRoute.isHandledByLivewire: Boolean  get() {
    val containingClass = method.containingClass
    return containingClass != null && containingClass.fqn == Livewire.COMPONENT_FQN
}

private fun ControllerRoute.navigateToLivewireComponent(requestFocus: Boolean) {
    val renderMethod = `class`.methods.find { it.name == "render" }
    if (renderMethod != null) {
        renderMethod.navigate(requestFocus)
        return
    }

    `class`.navigate(requestFocus)
}