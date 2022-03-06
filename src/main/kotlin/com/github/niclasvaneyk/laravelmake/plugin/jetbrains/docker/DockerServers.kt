package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.docker

import com.intellij.docker.DockerCloudConfiguration
import com.intellij.docker.DockerCloudType
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.configuration.RemoteServersManager

fun findFirstLocalDockerServer(): RemoteServer<DockerCloudConfiguration>? {
    val server = RemoteServersManager.getInstance().servers.firstOrNull {
        it.type.id == DockerCloudType.getInstance().id
    }

    if (server != null) {
        return server as RemoteServer<DockerCloudConfiguration>
    }

    return null
}