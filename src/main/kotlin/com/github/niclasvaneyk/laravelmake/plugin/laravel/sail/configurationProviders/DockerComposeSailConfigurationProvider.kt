package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider
import com.intellij.docker.DockerCloudType
import com.intellij.docker.DockerDeploymentConfiguration
import com.intellij.docker.DockerRunConfigurationCreator
import com.intellij.docker.compose.configuration.beans.common.DockerComposeConfiguration
import com.intellij.docker.deployment.DockerRunConfigFactory
import com.intellij.docker.deployment.DockerRunConfigurationManager
import com.intellij.docker.deploymentSource.DockerComposeDeploymentSourceType
import com.intellij.docker.deploymentSource.DockerDeploymentConfigurator
import com.intellij.docker.runtimes.DockerDeployer
import com.intellij.docker.runtimes.DockerRuntimesManagerService
import com.intellij.docker.runtimes.DockerRuntimesManagerService.DockerRuntimesManager
import com.intellij.remoteServer.impl.configuration.deployment.DeployToServerConfigurationType.SingletonTypeConfigurationFactory
import com.jetbrains.php.remote.docker.DockerComposeCredentialsPhpContributor
import com.jetbrains.php.remote.docker.PhpDockerContainerSettingsManager

class DockerComposeSailConfigurationProvider: SailConfigurationProvider {
    override fun apply(application: LaravelApplication) {
        val configurationCreator = DockerRunConfigurationCreator(application.project)

        val deploymentSourceType = DockerComposeDeploymentSourceType.getInstance()

        val conf = DockerDeploymentConfiguration()
        val factory = DockerCloudType.getRunConfigurationType().getFactoryForType(deploymentSourceType) as SingletonTypeConfigurationFactory
        val template = factory.createTemplateConfiguration(application.project)

        val foo = "bar"

//        configurationCreator.rememberConfiguration(config)
    }
}