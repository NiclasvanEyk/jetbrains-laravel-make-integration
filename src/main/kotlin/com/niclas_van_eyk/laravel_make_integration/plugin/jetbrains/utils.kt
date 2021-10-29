package com.niclas_van_eyk.laravel_make_integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.LaravelProject

fun targetFileFromEvent(event: AnActionEvent): VirtualFile? {
    val view = LangDataKeys.IDE_VIEW.getData(event.dataContext) ?: return null
    val directories = view.directories

    return if (directories.isNotEmpty()) directories[0].virtualFile else null
}

fun resolveLaravelProject(event: AnActionEvent): LaravelProject? {
    val targetFile = targetFileFromEvent(event) ?: return null
    val project = event.project ?: return null
    val projectBasePath = project.basePath ?: return null
    val artisanDirectory = findArtisanBinaryDirectory(targetFile, projectBasePath) ?: return null

    return LaravelProject(artisanDirectory, project)
}

fun findArtisanBinaryDirectory(start: VirtualFile, stop: String): VirtualFile? {
    var node = start
    var found = false

    while (!node.canonicalPath.equals(stop) || !node.exists()) {
        if (node.findFileByRelativePath("artisan") === null) {
            node = node.parent
            continue
        }

        found = true
        break
    }

    // Look for the binary one more time in the stop directory
    if (!found && node.findFileByRelativePath("artisan") !== null) {
        found = true
    }

    if (!found) return null

    return node
}
