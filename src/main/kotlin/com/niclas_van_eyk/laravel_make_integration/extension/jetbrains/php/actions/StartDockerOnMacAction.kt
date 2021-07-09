package com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.php.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.niclas_van_eyk.laravel_make_integration.common.macos.docker.DockerForMac

class StartDockerOnMacAction : AnAction("Start Docker") {
    override fun actionPerformed(e: AnActionEvent) {
        DockerForMac.start()
    }
}
