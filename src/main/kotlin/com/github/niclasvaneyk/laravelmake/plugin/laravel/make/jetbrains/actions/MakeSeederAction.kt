package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains.actions

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.DirectoryResolver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.SubCommand
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains.ArtisanMakeSubCommandActionExecution
import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.common.laravel.LaravelProjectPaths.Companion as Paths

/**
 * For BC reasons, this command is somewhat dynamic in how it determines
 * its [SubCommand.location].
 *
 * Maybe this should always be computed at runtime, since the artisan
 * commands also do the same.
 */
class MakeSeederAction : ArtisanMakeSubCommandAction(
    SubCommand("seeder", Paths.LEGACY_SEEDERS)
) {
    override fun buildExecution(
        meta: SubCommand,
        project: Project,
        laravelApplication: LaravelApplication,
        targetFilePath: String?
    ): ArtisanMakeSubCommandActionExecution {
        val dynamicCommand = SubCommand(
            meta.command,
            defaultDirectory(laravelApplication),
        )

        return super.buildExecution(dynamicCommand, project, laravelApplication, targetFilePath)
    }

    private fun defaultDirectory(app: LaravelApplication): String {
        if (app.paths.exists(Paths.LEGACY_SEEDERS)) {
            return Paths.LEGACY_SEEDERS
        }

        return Paths.SEEDERS
    }

    override fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String, app: LaravelApplication): Boolean {
        return DirectoryResolver(defaultDirectory(app)).couldPointToDefaultFolder(relativePathFromProjectRoot)
    }
}
