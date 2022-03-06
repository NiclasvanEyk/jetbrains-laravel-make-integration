package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.intellij.docker.DockerServerRuntimeInstance
import com.intellij.docker.DockerServerRuntimeInstance.DockerServerListener
import com.intellij.docker.agent.events.DockerEvent

// TODO: Remove
class DockerListener: DockerServerListener {
    override fun onDockerEvent(runtime: DockerServerRuntimeInstance, event: DockerEvent) {
        val foo = "bar"
    }
}