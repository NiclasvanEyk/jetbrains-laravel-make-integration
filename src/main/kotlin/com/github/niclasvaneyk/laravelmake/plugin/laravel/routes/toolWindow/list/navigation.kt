package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow.list

import com.github.niclasvaneyk.laravelmake.common.laravel.Livewire
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ClosureRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path

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

fun ClosureRoute.navigate(project: Project, requestFocus: Boolean = true) {
    val fs = LocalFileSystem.getInstance()
    val virtualFile = fs.findFileByNioFile(Path.of(project.basePath, action.file)) ?: return
    OpenFileDescriptor(project, virtualFile, action.start, 0).navigate(requestFocus)
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