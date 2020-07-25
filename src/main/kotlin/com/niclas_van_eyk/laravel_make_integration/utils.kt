package com.niclas_van_eyk.laravel_make_integration

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.niclas_van_eyk.laravel_make_integration.filesystem.findArtisanBinaryDirectory
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject as LaravelProject

fun targetFileFromEvent(event: AnActionEvent): VirtualFile? {
    val view = LangDataKeys.IDE_VIEW.getData(event.dataContext) ?: return null
    val directories = view.directories
    return directories[0].virtualFile
}

fun resolveLaravelProject(event: AnActionEvent): LaravelProject? {
    val targetFile = targetFileFromEvent(event) ?: return null
    val projectBasePath = event.project?.basePath ?: return null
    val artisanDirectory = findArtisanBinaryDirectory(targetFile, projectBasePath) ?: return null

    return LaravelProject(artisanDirectory)
}

fun tryToOpenFile(project: Project, path: String) {
    val file = LocalFileSystem.getInstance().refreshAndFindFileByPath(path)

    if (file != null) {
        ApplicationManager.getApplication().runReadAction {
            OpenFileDescriptor(project, file).navigate(true)
        }
    }
}