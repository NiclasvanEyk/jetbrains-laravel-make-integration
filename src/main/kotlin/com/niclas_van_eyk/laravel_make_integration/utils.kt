package com.niclas_van_eyk.laravel_make_integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.niclas_van_eyk.laravel_make_integration.filesystem.findArtisanBinaryDirectory
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject as LaravelProject

fun targetFileFromEvent(event: AnActionEvent): VirtualFile? {
    val view = LangDataKeys.IDE_VIEW.getData(event.dataContext) ?: return null
    val directories = view.directories

    return if (directories.isNotEmpty()) directories[0].virtualFile else null
}

fun resolveLaravelProject(event: AnActionEvent): LaravelProject? {
    val targetFile = targetFileFromEvent(event) ?: return null
    val projectBasePath = event.project?.basePath ?: return null
    // TODO: Maybe this should be done once while opening the project instead of over and over again?
    val artisanDirectory = findArtisanBinaryDirectory(targetFile, projectBasePath) ?: return null

    return LaravelProject(artisanDirectory)
}
