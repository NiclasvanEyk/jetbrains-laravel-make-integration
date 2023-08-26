package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker

import com.github.niclasvaneyk.laravelmake.common.system.env
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.docker.findFirstLocalDockerServer
import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.execution.configuration.EnvironmentVariablesData
import com.sun.security.auth.module.UnixSystem
import java.nio.file.Path

const val SAIL_LARAVEL_SERVICE_NAME = "laravel.test"

fun inferSailComposeCredentials(composeFilePath: String): DockerComposeCredentialsHolder? {
    val dockerAccount = findFirstLocalDockerServer() ?: return null
    val user = UnixSystem()

    return DockerComposeCredentialsHolder().apply {
        accountName = dockerAccount.name
        composeFilePaths = arrayListOf(composeFilePath)
        composeServiceName = SAIL_LARAVEL_SERVICE_NAME
        remoteProjectPath = "/opt/project"
        envs = EnvironmentVariablesData.create(mapOf(
            "WWWUSER" to env("WWWUSER", user.uid.toString()),
            "WWWGROUP" to env("WWWGROUP", user.gid.toString())
        ), true, Path.of(composeFilePath).parent.resolve(".env").toString())
    }
}