package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.actions.*
import com.niclas_van_eyk.laravel_make_integration.filesystem.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.ComposerVersion
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProjectPaths
import java.io.File

val VERSION_THAT_INTRODUCED_MODEL_DIRECTORY = ComposerVersion(8, 0, 0)

class MakeModelCreatedFileResolver(
        protected override val project: LaravelProject
): CreatedFileResolver(project) {
    override fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String? {
        var path = project.paths.base + command.location + "/"
        val filename = parameters.className + ".php"

        if (project.version.greaterOrEqualThan(VERSION_THAT_INTRODUCED_MODEL_DIRECTORY)) {
            path += "Models/"
        }

        return path + filename
    }
}

class MakeModelTargetResolver(
    override val directoryResolver: DirectoryResolver,
    val laravelVersion: ComposerVersion
): TargetResolver(directoryResolver) {
    override fun suggestInitialInputFor(target: String?, projectBasePath: String): InitialInputSuggestion {
        val suggested = super.suggestInitialInputFor(target, projectBasePath)
        // The suggested path will contain a Models/ prefix, if the user right-clicked below the Models/ folder

        // Since Laravel 8 the make:model command creates the files in app/Models/ by default, so we can safely
        // remove the prefix for higher versions
        val defaultsToModelDirectoryIfPresent = laravelVersion.greaterOrEqualThan(VERSION_THAT_INTRODUCED_MODEL_DIRECTORY)

        if (defaultsToModelDirectoryIfPresent) {
            return InitialInputSuggestion(suggested.name.removePrefix("Models/"))
        }

        // Earlier versions of the command do not implement this behavior. We emulate it
        // by not removing the suggested prefix if the model directory exists, or adding it if needed.
        val modelDirectoryExists = File(projectBasePath + LaravelProjectPaths.MODELS).exists()
        val prefix = if(suggested.name.startsWith("Models/")) "" else "Models/"

        if (modelDirectoryExists) {
            return InitialInputSuggestion(prefix + suggested.name)
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
        get() = MakeModelTargetResolver(directoryResolver, laravelProject.version)

    override val createdFileResolver: CreatedFileResolver
        get() = MakeModelCreatedFileResolver(laravelProject)
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
    // An exception is made for the /app/Models directory, as Laravel stores models there in
    // versions after 8.0.0
    override fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return arrayOf(LaravelProjectPaths.APP, LaravelProjectPaths.MODELS).contains(relativePathFromProjectRoot)
    }
}