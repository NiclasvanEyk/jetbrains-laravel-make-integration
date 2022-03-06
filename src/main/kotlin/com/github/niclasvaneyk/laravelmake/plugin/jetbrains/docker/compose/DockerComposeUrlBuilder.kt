package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.docker.compose

import com.intellij.docker.remote.DockerComposeCredentialsType

class DockerComposeFile(private val composeFilePath: String) {
    companion object {
        /**
         * See e.g. [DockerComposeCredentialsType.DOCKER_COMPOSE_PREFIX]
         */
        const val PROTOCOL = "docker-compose://"
    }

    fun uri(service: String, path: String): String {
        return "${PROTOCOL}[$composeFilePath]:$service/$path"
    }
}