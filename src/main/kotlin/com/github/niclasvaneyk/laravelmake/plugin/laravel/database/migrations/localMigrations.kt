package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.common.laravel.LaravelProjectPaths
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import java.io.File

data class MigrationFile(val file: File) {
    val name get() = file.nameWithoutExtension
}

val LaravelApplication.localMigrations: List<MigrationFile> get() {
    return File(paths.path(LaravelProjectPaths.MIGRATIONS))
        .listFiles()
        .map { MigrationFile(it) }
        .toList()
}