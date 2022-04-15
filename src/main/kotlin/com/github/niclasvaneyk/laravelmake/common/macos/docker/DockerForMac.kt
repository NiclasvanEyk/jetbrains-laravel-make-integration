package com.github.niclasvaneyk.laravelmake.common.macos.docker

import com.intellij.openapi.util.SystemInfo
import com.github.niclasvaneyk.laravelmake.common.macos.filesystem.MacOsDirectories
import java.awt.Desktop
import java.nio.file.Paths

class DockerForMac {
    companion object {
        private val appDirectory get() = Paths
            .get(MacOsDirectories.applications, "Docker.app")
            .toFile()

        fun isAvailable() = SystemInfo.isMac && appDirectory.exists()
        fun start() = Desktop.getDesktop().open(appDirectory)
    }
}
