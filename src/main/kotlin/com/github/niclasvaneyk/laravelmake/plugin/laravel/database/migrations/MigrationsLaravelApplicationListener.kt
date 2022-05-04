package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplicationListener
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
    }

    override fun event(name: String) {
    }
}