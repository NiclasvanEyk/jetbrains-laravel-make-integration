package com.github.niclasvaneyk.laravelmake.support

import com.github.niclasvaneyk.laravelmake.support.filesystem.temporaryDirectory
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightProjectDescriptor
import java.nio.file.Paths

/**
 * Ensures, that the module contains a fresh Laravel application.
 *
 * This might entail installing Laravel via the recommended method,
 * requiring Docker to be present on the system.
 */
class LaravelProjectDescriptor: LightProjectDescriptor() {
    private val laravelDownloader = LaravelProjectDownloader()

    override fun createMainModule(project: Project): Module {
        val projectDirectory = temporaryDirectory("laravel-make-test-project").apply { mkdirs() }
        laravelDownloader.installContentsTo(projectDirectory)

        return createModule(project, projectDirectory.toPath().resolve("laravel-make-test.iml"))
    }

    override fun createDirForSources(module: Module): VirtualFile? = null
}