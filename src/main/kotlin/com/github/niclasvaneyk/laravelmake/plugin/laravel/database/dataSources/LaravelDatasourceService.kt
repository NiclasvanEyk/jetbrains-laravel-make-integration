package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

val LaravelApplication.dataSources: LaravelDatasourceService get() {
    return project.service()
}

@Service
class LaravelDatasourceService(private val project: Project) {
    /**
     * The [LocalDataSource] managed by this plugin.
     */
    val managed: LocalDataSource? get() = LocalDataSourceManager
        .getInstance(project)
        .dataSources
        .firstOrNull { it.comment == LaravelDataSourceAutoConfigurer.MANAGED_DATA_SOURCE_MARKER_COMMENT }
}