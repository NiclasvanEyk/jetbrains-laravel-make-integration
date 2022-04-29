package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources.dataSources
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.database.dataSource.DatabaseConnectionManager
import com.intellij.javascript.nodejs.execution.runExecutionUnderProgress
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import java.io.File

data class Migration(
    val file: File,
    val batch: Int?,
) {
    val name get() = file.nameWithoutExtension
    val ran get() = batch != null
}

class RefreshMigrationAction: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val migrations = e.project?.service<MigrationService>() ?: return
        migrations.refresh()
    }
}

@Service
class MigrationService(private val project: Project) {
    private val laravel = project.laravel()!!
    private var migrations = emptyList<Migration>()
    val current get() = migrations

    /**
     * Refreshes the migrations and calls [done] upon termination.
     */
    fun refresh(done: ((List<Migration>) -> Unit)? = null) {
        val db = laravel.dataSources.managed ?: return

        runExecutionUnderProgress(project, "Introspecting migrations...") {
            val manager = DatabaseConnectionManager.getInstance()
            val connection = manager.build(project, db).create()?.get() ?: return@runExecutionUnderProgress

            connection.getDatabaseMigrations { migrationTableEntries ->
                val byName = migrationTableEntries.associateBy { it.name }
                migrations = laravel.localMigrations
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
}