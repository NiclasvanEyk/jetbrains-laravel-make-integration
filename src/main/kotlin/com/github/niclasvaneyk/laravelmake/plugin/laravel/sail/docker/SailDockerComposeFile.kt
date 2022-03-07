package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.docker.compose.DockerComposeFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication

class SailDockerComposeFile(val path: String) {
    private val composeFile = DockerComposeFile(path)

    constructor(application: LaravelApplication) : this(application.paths.path("docker-compose.yml"))

    fun uri(path: String) = composeFile.uri(SAIL_LARAVEL_SERVICE_NAME, path)
}
