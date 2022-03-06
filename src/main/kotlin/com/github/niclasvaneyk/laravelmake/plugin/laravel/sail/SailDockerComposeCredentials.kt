package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.docker.findFirstLocalDockerServer
import com.intellij.docker.remote.DockerComposeCredentialsHolder

const val SAIL_LARAVEL_SERVICE_NAME = "laravel.test"

fun inferSailComposeCredentials(composeFilePath: String): DockerComposeCredentialsHolder? {
    val dockerAccount = findFirstLocalDockerServer() ?: return null

    return DockerComposeCredentialsHolder().apply {
        accountName = dockerAccount.name
        composeFilePaths = arrayListOf(composeFilePath)
        composeServiceName = SAIL_LARAVEL_SERVICE_NAME
        remoteProjectPath = "/opt/project"
    }
}