package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelInstaller

import com.github.niclasvaneyk.laravelmake.common.filesystem.moveDirectoryContents
import com.github.niclasvaneyk.laravelmake.common.filesystem.temporaryDirectory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Creates a new project using the Laravel installer.
 *
 * Currently, no options are supported, but this may change in the future.
 */
class LaravelInstallerGenerationStrategy(
    settings: LaravelInstallerSettings,
): LaravelProjectGenerationStrategy<LaravelInstallerSettings>(settings) {
    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        path: String,
        module: com.intellij.openapi.module.Module
    ) {
        val sourcesDirectory = temporaryDirectory()

        val process = ProcessBuilder()
            .directory(sourcesDirectory)
            .command(
                settings.executablePath,
                "new",
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