package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations.MigrationService
import com.intellij.openapi.components.service

class UpdateLaravelMakeDataAction: LaravelAction() {
    init {
        templatePresentation.description = "Refreshes data for routes, commands, migrations, etc."
    }

    override fun run(application: LaravelApplication) {
        application.project.also {
            // For some reason cannot be created...
            service<MigrationService>().refresh()
        }
        application.introspection.refresh()
    }
}