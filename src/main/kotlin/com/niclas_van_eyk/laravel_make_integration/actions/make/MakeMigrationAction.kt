package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandAction
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandActionExecution
import com.niclas_van_eyk.laravel_make_integration.actions.SubCommand
import com.niclas_van_eyk.laravel_make_integration.filesystem.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProjectPaths
import java.io.File

/**
 * This just sorts the files in the migrations-folder and returns the last one.
 * Because Laravel prefixes the migrations with timestamps, the freshly created one should
 * be the one with the most current timestamp, so this will work like 99% of the time.
 */
class MigrationCreatedFileResolver(
    override val project: LaravelProject
): CreatedFileResolver(project) {
    override fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String? {
        val migrationFolder = File(project.paths.path(LaravelProjectPaths.MIGRATIONS))

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
): ArtisanMakeSubCommandActionExecution(
    command,
    project,
    laravelProject,
    target
) {
    override val createdFileResolver: CreatedFileResolver
        get() = MigrationCreatedFileResolver(laravelProject)
}

class MakeMigrationAction: ArtisanMakeSubCommandAction(
    SubCommand("migration", LaravelProjectPaths.MIGRATIONS)
) {
    override fun buildExecution(meta: SubCommand, project: Project, laravelProject: LaravelProject, targetFilePath: String?): ArtisanMakeSubCommandActionExecution {
        return MakeMigrationActionExecution(meta, project, laravelProject, targetFilePath)
    }
}