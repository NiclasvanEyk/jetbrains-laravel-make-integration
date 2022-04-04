package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelBuild

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class LaravelBuildGenerationStrategy(override val settings: LaravelBuildSettings)
    : LaravelProjectGenerationStrategy<LaravelBuildSettings>(settings) {
    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        path: String,
        module: com.intellij.openapi.module.Module
    ) {
        TODO()
    }
}