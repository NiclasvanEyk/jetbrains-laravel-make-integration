package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources.dataSources
import com.intellij.openapi.components.service

class MigrationsLaravelApplicationListener: LaravelApplicationListener {
    override fun initialized(application: LaravelApplication) {
        val migrations = application.project.service<MigrationService>().apply {
            refresh()
        }

        // TODO: This might not be necessary
        migrations.onMigrationFileChanged {
            migrations.refresh()
        }

        migrations.onMigrationsEnded {
            // After the migrations ran, it makes sense to refresh local
            // data sources, since we can be fairly sure something has
            // changed.
            application.dataSources.syncManaged()
            migrations.refresh()
        }
    }

}