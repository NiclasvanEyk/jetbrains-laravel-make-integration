package com.github.niclasvaneyk.laravelmake.support

import com.github.niclasvaneyk.laravelmake.support.filesystem.temporaryDirectory
import com.intellij.testFramework.LightProjectDescriptor
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit.MINUTES

class LaravelProjectDownloader {
    fun installContentsTo(directory: File) {
        val cache = temporaryDirectory("laravel-make-tests-template-project")

        if (!cache.exists()) {
            println("No Laravel project was found on disk, creating one (this might take a while and requires docker to be present on the system)...")
            download(cache)
        }

        copyDirectoryContents(from = cache, to = directory)
    }

    private fun copyDirectoryContents(from: File, to: File) {
        FileUtils.copyDirectoryToDirectory(from, to)
    }

    private fun download(targetDirectory: File) {
        if (targetDirectory.exists()) targetDirectory.deleteRecursively()

        val installationProcess = ProcessBuilder("curl", "-s", "https://laravel.build/${targetDirectory.name} | bash")
            .directory(targetDirectory.parentFile)
            .inheritIO()
            .start()

        installationProcess.waitFor(15, MINUTES)

        if (installationProcess.exitValue() != 0) {
            throw Exception("The installation process did not terminate successfully. Additional output should have been printed above.")
        }
    }
}