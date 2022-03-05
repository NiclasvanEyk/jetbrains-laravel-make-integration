package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider
import com.intellij.docker.remote.DockerComposeCredentialsEditor
import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.docker.remote.DockerContainerSettings
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.remote.docker.PhpDockerContainerSettingsManager
import com.jetbrains.php.remote.interpreter.PhpRemoteInterpreterFactory
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData

class PhpSailConfigurationProvider: SailConfigurationProvider {
    companion object {
        const val SAIL_INTERPRETER_ID = "laravel-make-sail"
        const val SAIL_INTERPRETER_NAME = "Laravel Sail"
        const val SAIL_PHP_SERVICE_NAME = "laravel.test"
        const val SAIL_PHP_INTERPRETER_PATH = "php"
    }

    override fun apply(application: LaravelApplication) {
        val interpreters = PhpInterpretersManagerImpl.getInstance(application.project)

        val existingInterpreter = interpreters.findInterpreterId(SAIL_INTERPRETER_ID)
        if (existingInterpreter != null) {
            // TODO: Notification
            return
        }

        createSailInterpreter(application, interpreters)
    }

    private fun createSailInterpreter(application: LaravelApplication, interpreters: PhpInterpretersManagerImpl) {

        /** See [DockerComposeCredentialsEditor] */
        /** Also just set up an interpreter in a fresh Sail app and inspect it via [PhpInterpretersManagerImpl.getInterpreters] */

        val composeFilePath = application.paths.path("docker-compose.yml")
        val protocol = DockerComposeCredentialsType.DOCKER_COMPOSE_PREFIX
        val interpreter = PhpInterpreter().apply {
            id = SAIL_INTERPRETER_ID
            name = SAIL_INTERPRETER_NAME
            // There might be a better way to build this, but this is what it looks like when I manually create
            // an instance of a remote interpreter in a fresh Sail app
            homePath = "$protocol[$composeFilePath]:$SAIL_PHP_SERVICE_NAME/$SAIL_PHP_INTERPRETER_PATH"

            phpSdkAdditionalData = PhpRemoteSdkAdditionalData().apply {
                interpreterId = SAIL_INTERPRETER_ID
            }
        }



        val dockerCredentials = DockerComposeCredentialsHolder().apply {
            accountName = "TODO" // TODO
            composeFilePaths = arrayListOf(composeFilePath)
            composeServiceName = SAIL_PHP_SERVICE_NAME
            remoteProjectPath = "/opt/project"
        }

        val containerSettings = DockerContainerSettings()

//        val interpreterFactory = PhpRemoteInterpreterFactory().createRemoteSdk<>(project, )

        PhpDockerContainerSettingsManager
            .getInstance(application.project)
            .setSettings(SAIL_INTERPRETER_ID, containerSettings)
        interpreters.addInterpreter(interpreter)
    }
}