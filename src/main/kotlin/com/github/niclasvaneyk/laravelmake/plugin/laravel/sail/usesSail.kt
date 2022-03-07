package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import java.io.File

/**
 * Determines whether it is likely that the project uses Laravel Sail.
 */
fun LaravelApplication.usesSail(): Boolean {
    val composerJson = File(paths.path("composer.json"))
    val composerJsonContainsSail = composerJson.isFile && composerJson
        .readText()
        .contains("\"laravel/sail\"")

    val dockerComposeYml = File(paths.path("docker-compose.yml"))
    val composeContainsLaravelService = dockerComposeYml.isFile && dockerComposeYml
        .readText()
        .contains("laravel.test")

    return composerJsonContainsSail && composeContainsLaravelService
}