package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.common.laravel.events.Events
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources.dataSources
import com.github.niclasvaneyk.laravelmake.plugin.laravel.events.connector.events
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File

data class Migration(val file: File, val batch: Int?) {
    val name get() = file.nameWithoutExtension
    val ran get() = batch != null
}

@Service(Service.Level.PROJECT)
class MigrationService(private val project: Project) {
    private val application = project.laravel()!!
    private var migrations = emptyList<Migration>()

    val current get() = migrations

    /**
     * Refreshes the migrations and calls [done] upon termination.
     */
    fun refresh(done: ((List<Migration>) -> Unit)? = null) {
        val db = application.dataSources.managed ?: return

        runBackgroundableTask("Introspecting migrations...", project) {
            val manager = DatabaseConnectionManager.getInstance()
            val connection = manager.build(project, db).create()?.get() ?: return@runBackgroundableTask

            connection.getDatabaseMigrations { migrationTableEntries ->
                val byName = migrationTableEntries.associateBy { it.name }
                migrations = application.localMigrations
                    .map {
                        Migration(file = it.file, batch = byName[it.name]?.batch)
                    }
                    .sortedBy { it.file.name }
                if (done != null) {
                    done(migrations)
                }
            }
        }
    }

    /**
     * Runs the [callback] after a file in the migrations folder changed.
     */
    fun onMigrationFileChanged(callback: () -> Unit) {
        VirtualFileManager.getInstance().addAsyncFileListener(
            { events ->
                for (event in events) {
                    if (event.path.matches(Regex(".*/migrations/.*\\.php"))) {
                        callback()
                        break
                    }
                }

                null
            },
            application.project,
        )
    }

    /**
     * Runs the [callback] after a migration was run by the application.
     */
    fun onMigrationsEnded(callback: () -> Unit) {
        application.events.subscribe(Events.MIGRATIONS_ENDED, callback)
    }
}