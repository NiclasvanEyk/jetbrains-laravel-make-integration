package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandAction
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandActionExecution
import com.niclas_van_eyk.laravel_make_integration.actions.SubCommand
import com.niclas_van_eyk.laravel_make_integration.actions.TargetResolver
import com.niclas_van_eyk.laravel_make_integration.filesystem.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProjectPaths
import java.io.File

class MakeModelTargetResolver(
    override val directoryResolver: DirectoryResolver
): TargetResolver(directoryResolver) {
    override fun suggestInitialInputFor(target: String?, projectBasePath: String): String {
        val suggested = super.suggestInitialInputFor(target, projectBasePath)

        // If the project contains a /app/Models directory, it is likely that the user wants to store
        // all the models there. Therefore we will prefix the suggested path with Models/ if we detect
        // that this folder exists.
        if (File(projectBasePath + LaravelProjectPaths.MODELS).exists()) {
            return "Models/$suggested"
        }

        // Otherwise we will just use the default implementation
        return suggested
    }
}

class MakeModelActionExecution(
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
    override val targetResolver: TargetResolver
        get() = MakeModelTargetResolver(directoryResolver)
}

class MakeModelAction: ArtisanMakeSubCommandAction(
    SubCommand("model", LaravelProjectPaths.APP)
) {
    override fun buildExecution(meta: SubCommand, project: Project, laravelProject: LaravelProject, targetFilePath: String?): ArtisanMakeSubCommandActionExecution {
        return MakeModelActionExecution(meta, project, laravelProject, targetFilePath)
    }

    // As the models are stored in /app by default, we should not just activate this action
    // whenever one right-clicks *somewhere* inside the app folder.
    // Otherwise this action gets activated, when you right click from /app/Http/Controllers,
    // which is not where one would expect a model.
    // An exception is made for the /app/Models directory, as this is a non-standard but commonly
    // used place for storing models.
    override fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return arrayOf(LaravelProjectPaths.APP, LaravelProjectPaths.MODELS).contains(relativePathFromProjectRoot)
    }
}