package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.github.niclasvaneyk.laravelmake.common.macos.docker.DockerForMac

class StartDockerOnMacAction : AnAction("Start Docker") {
    override fun actionPerformed(e: AnActionEvent) {
        DockerForMac.start()
    }
}
