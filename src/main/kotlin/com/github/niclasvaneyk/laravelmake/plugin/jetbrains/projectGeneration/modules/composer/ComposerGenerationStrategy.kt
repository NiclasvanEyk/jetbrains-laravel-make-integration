package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.composer

import com.github.niclasvaneyk.laravelmake.common.filesystem.moveDirectoryContents
import com.github.niclasvaneyk.laravelmake.common.filesystem.temporaryDirectory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Runs `composer create-project ...`.
 *
 * As composer does not support generating a project into an existing project,
 * we create the project in a temporary folder and move the contents over to
 * the project, once the generation is finished.
 */
class ComposerGenerationStrategy(settings: ComposerSettings): LaravelProjectGenerationStrategy<ComposerSettings>(settings) {
    override fun generateProject(project: Project, baseDir: VirtualFile, path: String, module: Module) {
        val sourcesDirectory = temporaryDirectory()

        val process = ProcessBuilder()
            .directory(sourcesDirectory)
            .command(
                settings.executablePath,
                "create-project",
                "--no-install",
                "laravel/laravel",
                settings.projectName,
            )
            .start()

        val commandLine = process
            .info()
            .commandLine()
            .orElseGet { "Generating..." }

        runModalTask(commandLine, project) {
            process.waitFor()
            moveDirectoryContents(
                sourcesDirectory.toPath().resolve(settings.projectName),
                baseDir.toNioPath(),
            )
        }
    }
}