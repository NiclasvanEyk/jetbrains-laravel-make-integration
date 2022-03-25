package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.ConfigurationIntrospector
import com.github.niclasvaneyk.laravelmake.common.php.run.ArbitraryPhpRunner
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem.LaravelAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.project.DumbAware

class SyncLaravelDataSourcesAction: LaravelAction(), DumbAware {
    override fun run(application: LaravelApplication) {
        LaravelDataSourceAutoConfigurer(
            application,
            ConfigurationIntrospector(ArbitraryPhpRunner(application.artisan)),
        ).updateManagedDataSource()
    }
}