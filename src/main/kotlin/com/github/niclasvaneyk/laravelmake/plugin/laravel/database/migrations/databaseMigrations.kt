package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.common.jetbrains.database.SqlExecutor
import com.intellij.database.dataSource.DatabaseConnection
import com.intellij.javascript.nodejs.execution.runExecutionUnderProgress

data class MigrationTableEntry(
    val id: Int,
    val name: String,
    val batch: Int,
)

fun DatabaseConnection.getDatabaseMigrations(
    done: (List<MigrationTableEntry>) -> Unit,
) {
    val entries = SqlExecutor(this).run("select * from migrations") {
        return@run MigrationTableEntry(
            id = it.getInt(1),
            name = it.getString(2),
            batch = it.getInt(3),
        )
    }
    done(entries)
}