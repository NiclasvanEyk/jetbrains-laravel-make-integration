package com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.jetbrains.actions

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.jetbrains.ArtisanMakeSubCommandActionExecution
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.common.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.common.laravel.LaravelProjectPaths
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.SubCommand
import java.io.File

/**
 * This just sorts the files in the migrations-folder and returns the last one.
 * Because Laravel prefixes the migrations with timestamps, the freshly created one should
 * be the one with the most current timestamp, so this will work like 99% of the time.
 */
class MigrationCreatedFileResolver(
    override val base: String,
) : CreatedFileResolver(base) {
    override fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String {
        val paths = LaravelProjectPaths(base)
        val migrationFolder = File(paths.path(LaravelProjectPaths.MIGRATIONS))

        val migrations = migrationFolder.list { _: File, file: String ->
            file.endsWith(".php")
        }

        migrations.sort()

        return migrationFolder.absolutePath + "/" + migrations.last()!!
    }
}

class MakeMigrationActionExecution(
    override val command: SubCommand,
    override val project: Project,
    override val laravelProject: LaravelProject,
    override val target: String?
) : ArtisanMakeSubCommandActionExecution(
    command,
    project,
    laravelProject,
    target
) {
    override val createdFileResolver: CreatedFileResolver
        get() = MigrationCreatedFileResolver(laravelProject.paths.base)
}

class MakeMigrationAction : ArtisanMakeSubCommandAction(
    SubCommand("migration", LaravelProjectPaths.MIGRATIONS)
) {
    override fun buildExecution(
        meta: SubCommand,
        project: Project,
        laravelProject: LaravelProject,
        targetFilePath: String?
    ): ArtisanMakeSubCommandActionExecution {
        return MakeMigrationActionExecution(meta, project, laravelProject, targetFilePath)
    }
}
