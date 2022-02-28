package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelInstaller

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class LaravelInstallerGenerationStrategy(settings: LaravelInstallerSettings): LaravelProjectGenerationStrategy<LaravelInstallerSettings>(settings) {
    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        path: String,
        module: com.intellij.openapi.module.Module
    ) {
        TODO("Not yet implemented")
    }
}