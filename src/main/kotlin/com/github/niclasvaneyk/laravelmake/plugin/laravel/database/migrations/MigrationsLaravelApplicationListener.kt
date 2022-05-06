package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.common.laravel.events.Events
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources.dataSources
import com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.events
import com.intellij.database.util.DataSourceUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFileManager

class MigrationsLaravelApplicationListener: LaravelApplicationListener {
    override fun initialized(application: LaravelApplication) {
        val service = application.project.service<MigrationService>()
        service.refresh()

        VirtualFileManager.getInstance().addAsyncFileListener(
            MigrationsFileChangeListener(service),
            application.project,
        )

        application.events.subscribe(Events.MIGRATIONS_ENDED) {
            // After the migrations ran, it makes sense to refresh local
            // data sources, since we can be fairly sure something has
            // changed.
            DataSourceUtil.performAutoSyncTask(
                application.project,
                application.dataSources.managed ?: return@subscribe
            )
            service.refresh()
        }
    }
}