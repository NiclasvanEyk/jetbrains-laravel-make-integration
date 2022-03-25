package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem.LaravelAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.project.DumbAware

class AutoconfigureDefaultDatasourceAction: LaravelAction(), DumbAware {
    override fun run(application: LaravelApplication) {
        LaravelDataSourceAutoConfigurer(application).updateManagedDataSource()
    }
}