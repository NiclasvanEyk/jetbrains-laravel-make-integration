package com.niclas_van_eyk.laravel_make_integration.common.macos.docker

import com.niclas_van_eyk.laravel_make_integration.common.macos.filesystem.MacOsDirectories
import java.awt.Desktop
import java.nio.file.Paths
import java.util.*

class DockerForMac {
    companion object {
        private val osName get() = System
            .getProperty("os.name", "generic")
            .toLowerCase(Locale.ENGLISH)

        private val runningOnMacos get() = osName.contains("mac os")

        private val appDirectory get() = Paths
            .get(MacOsDirectories.applications, "Docker.app")
            .toFile()

        fun isAvailable(): Boolean {
            return runningOnMacos && appDirectory.exists()
        }

        fun start() {
            Desktop.getDesktop().open(appDirectory)
        }
    }
}
