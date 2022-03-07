package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.configurationProviders

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailConfigurationProvider

class DockerComposeSailConfigurationProvider: SailConfigurationProvider {
    override fun configurationExists(application: LaravelApplication): Boolean {
        return true // TODO
    }

    override fun apply(application: LaravelApplication) {
//        val configurationCreator = DockerRunConfigurationCreator(application.project)
//
//        val deploymentSourceType = DockerComposeDeploymentSourceType.getInstance()
//
//        val conf = DockerDeploymentConfiguration()
//        val factory = DockerCloudType.getRunConfigurationType().getFactoryForType(deploymentSourceType) as SingletonTypeConfigurationFactory
//        val template = factory.createTemplateConfiguration(application.project)

        // This is intended to create a docker-compose run configuration to start
        // the laravel.test service.
        // Will be implemented at some point in the future
    }
}