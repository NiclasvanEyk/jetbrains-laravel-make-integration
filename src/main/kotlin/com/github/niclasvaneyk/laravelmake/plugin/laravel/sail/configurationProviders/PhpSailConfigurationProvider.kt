package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.SailDockerComposeFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.inferSailComposeCredentials
import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.openapi.diagnostic.logger
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.remote.docker.PhpDockerHelpersManager
import com.jetbrains.php.remote.docker.compose.PhpDockerComposeStartCommand
import com.jetbrains.php.remote.docker.compose.PhpDockerComposeTypeData
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData

/**
 * Sets up the PHP interpreter to use the one from the Sail container.
 */
class PhpSailConfigurationProvider: SailConfigurationProvider {
    companion object {
        const val SAIL_PHP_INTERPRETER_ID = "laravel-make-sail-php"
        const val SAIL_PHP_INTERPRETER_NAME = "Laravel Sail"
        private val log = logger<PhpSailConfigurationProvider>()
    }

    override fun configurationExists(application: LaravelApplication): Boolean {
        return sailInterpreterExists(application) && usesSailInterpreter(application)
    }

    override fun apply(application: LaravelApplication) {
        createSailInterpreter(application)
        useSailInterpreterAsProjectInterpreter(application)
    }

    private fun usesSailInterpreter(application: LaravelApplication): Boolean {
        val config = PhpProjectConfigurationFacade.getInstance(application.project)

        return config.interpreter?.id == SAIL_PHP_INTERPRETER_ID;
    }

    private fun useSailInterpreterAsProjectInterpreter(application: LaravelApplication) {
        val manager = PhpInterpretersManagerImpl.getInstance(application.project)
        val sailInterpreter = manager.findInterpreterById(SAIL_PHP_INTERPRETER_ID)
        if (sailInterpreter == null) {
            log.warn("Could not find the pre-configured Sail interpreter, so it can not be selected, aborting...")
            return
        }

        val config = PhpProjectConfigurationFacade.getInstance(application.project)
        config.updateSelectedInterpreterName(config.interpreter?.name, sailInterpreter.name)
        log.info("Successfully selected Sail interpreter '${sailInterpreter.id}' for the project!")
    }

    private fun sailInterpreterExists(application: LaravelApplication): Boolean {
        val interpreters = PhpInterpretersManagerImpl.getInstance(application.project)

        return interpreters.interpreters.firstOrNull { it.id == SAIL_PHP_INTERPRETER_ID } != null
    }

    private fun createSailInterpreter(application: LaravelApplication) {
        if (sailInterpreterExists(application)) {
            log.info("Sail interpreter already exists, I won't create a new one")
            return
        }

        val interpreters = PhpInterpretersManagerImpl.getInstance(application.project)
        val builder = SailComposePhpInterpreterBuilder(SailDockerComposeFile(application))
        val sailPhpInterpreter = builder.build(SAIL_PHP_INTERPRETER_ID, SAIL_PHP_INTERPRETER_NAME)

        if (sailPhpInterpreter != null) {
            interpreters.addInterpreter(sailPhpInterpreter)
            log.info("Added Sail interpreter '${sailPhpInterpreter.id}'!")
        } else {
            log.warn("Could not build Sail interpreter. Maybe somethings wrong with the docker-compose plugin is not available/accessible?")
            // TODO: Notify user?
        }
    }
}

class SailComposePhpInterpreterBuilder(private val composeFile: SailDockerComposeFile) {
    companion object {
        const val SAIL_PHP_INTERPRETER_PATH = "php"
        /** See [PhpDockerHelpersManager] */
        const val PHP_DOCKER_HELPERS_PATH = "/opt/.phpstorm_helpers"
    }

    fun build(id: String, name: String): PhpInterpreter? {
        val composeCredentials = composeCredentials() ?: return null

        return PhpInterpreter().apply {
            this@apply.id = id
            this@apply.name = name
            setIsProjectLevel(true)
            homePath = composeFile.uri(SAIL_PHP_INTERPRETER_PATH)
            phpSdkAdditionalData = sdkAdditionalData(id, composeCredentials)
        }
    }

    private fun composeCredentials(): DockerComposeCredentialsHolder? {
        return inferSailComposeCredentials(composeFile.path)
    }

    private fun sdkAdditionalData(
        interpreterId: String,
        composeCredentials: DockerComposeCredentialsHolder
    ): PhpRemoteSdkAdditionalData {
        return PhpRemoteSdkAdditionalData().apply {
            this@apply.interpreterId = interpreterId
            typeData = PhpDockerComposeTypeData(PhpDockerComposeStartCommand.RUN)
            helpersPath = PHP_DOCKER_HELPERS_PATH
            interpreterPath = SAIL_PHP_INTERPRETER_PATH
            connectionCredentials().setCredentials(
                DockerComposeCredentialsType.DOCKER_COMPOSE_CREDENTIALS,
                composeCredentials,
            )
        }
    }
}
