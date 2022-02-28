package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

abstract class LaravelProjectGenerationStrategy<S>(open val settings: S) {
    abstract fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        path: String,
        module: Module
    )
}
