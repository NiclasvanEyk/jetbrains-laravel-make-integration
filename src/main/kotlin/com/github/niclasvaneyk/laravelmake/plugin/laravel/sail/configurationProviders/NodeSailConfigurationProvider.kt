package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.SAIL_LARAVEL_SERVICE_NAME
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.SailDockerComposeFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker.inferSailComposeCredentials
import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef
import com.jetbrains.nodejs.remote.NodeJSRemoteSdkAdditionalData
import com.intellij.javascript.nodejs.interpreter.remote.NodeJsRemoteInterpreter
import com.intellij.javascript.nodejs.npm.NpmManager
import com.intellij.javascript.nodejs.util.NodePackage
import com.intellij.javascript.nodejs.util.NodePackageRef
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.jetbrains.nodejs.remote.NodeRemoteInterpreters

private val LaravelApplication.sailNodeRemoteInterpreter get() = NodeJsRemoteInterpreter(SailDockerComposeFile(this).uri("node"))

/**
 * Setups up NodeJs and NPM to use the binaries from the Sail container.
 */
class NodeSailConfigurationProvider: SailConfigurationProvider {
    companion object {
        /**
         * The path inside the sail container that needs to be configured.
         */
        const val NPM_PATH = "/usr/lib/node_modules/npm"
        private val log = logger<NodeSailConfigurationProvider>()
    }

    override fun configurationExists(application: LaravelApplication): Boolean {
        return findSailInterpreter() != null
            && usesSailInterpreter(application)
            && hasNpmPathConfigured(application)
    }

    private fun usesSailInterpreter(application: LaravelApplication): Boolean {
        val interpreter = NodeJsInterpreterManager.getInstance(application.project).interpreter ?: false
        if (interpreter !is NodeJsRemoteInterpreter) return false

        return interpreter.remoteUrl == application.sailNodeRemoteInterpreter.remoteUrl
    }

    override fun apply(application: LaravelApplication) {
        val sailInterpreter = findSailInterpreter()
        if (sailInterpreter == null) {
            log.info("No not find existing Sail interpreter, trying to create a new one...")
            createSailInterpreter(application) ?: return
        }

        configureNodeInterpreter(application)
        configureNpmPath(application)
    }

    private fun configureNodeInterpreter(application: LaravelApplication) {
        val interpreterReference = NodeJsInterpreterRef.create(application.sailNodeRemoteInterpreter)
        NodeJsInterpreterManager.getInstance(application.project).setInterpreterRef(interpreterReference)
    }

    private fun configureNpmPath(application: LaravelApplication) {
        val npmManager = application.project.service<NpmManager>()
        npmManager.packageRef = NodePackageRef.create(NodePackage(NPM_PATH))
    }

    private fun createSailInterpreter(application: LaravelApplication): NodeJSRemoteSdkAdditionalData? {
        val sailRemoteInterpreter = SailComposeNodeInterpreterBuilder(SailDockerComposeFile(application)).build()
        if (sailRemoteInterpreter == null) {
            log.warn("Could not build Sail interpreter. Maybe somethings wrong with the docker-compose plugin is not available/accessible?")
            // TODO: maybe throw?
            return null
        }

        return sailRemoteInterpreter.also {
            NodeRemoteInterpreters.getInstance().add(it)
        }
    }

    private fun findSailInterpreter(): NodeJSRemoteSdkAdditionalData? {
        return NodeRemoteInterpreters.getInstance().interpreters.firstOrNull {
            val credentials = it.connectionCredentials().credentials
            if (credentials !is DockerComposeCredentialsHolder) return@firstOrNull false

            return@firstOrNull credentials.composeServiceName == SAIL_LARAVEL_SERVICE_NAME
        }
    }

    private fun hasNpmPathConfigured(application: LaravelApplication): Boolean {
        val npmManager = application.project.service<NpmManager>()
        return npmManager.packageRef.constantPackage?.systemIndependentPath == NPM_PATH
    }
}

class SailComposeNodeInterpreterBuilder(private val composeFile: SailDockerComposeFile) {
    companion object {
        const val SAIL_NODE_INTERPRETER_PATH = "node"
        const val SAIL_NODE_HELPERS_PATH = ".webstorm_nodejs_helpers"
    }

    fun build(): NodeJSRemoteSdkAdditionalData? {
        val composeCredentials = composeCredentials() ?: return null

        return NodeJSRemoteSdkAdditionalData(SAIL_NODE_INTERPRETER_PATH).apply {
            helpersPath = "TODO"
//            sdkId = id
            helpersPath = SAIL_NODE_HELPERS_PATH
            connectionCredentials().setCredentials(
                DockerComposeCredentialsType.DOCKER_COMPOSE_CREDENTIALS,
                composeCredentials,
            )
        }
    }

    private fun composeCredentials(): DockerComposeCredentialsHolder? {
        return inferSailComposeCredentials(composeFile.path)
    }
}