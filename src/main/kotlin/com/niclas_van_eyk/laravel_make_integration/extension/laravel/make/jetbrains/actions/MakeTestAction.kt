package com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.jetbrains.actions

import com.intellij.openapi.project.Project
import com.niclas_van_eyk.laravel_make_integration.common.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.LaravelProject
import com.niclas_van_eyk.laravel_make_integration.common.laravel.LaravelProjectPaths
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.SubCommand
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.jetbrains.*

/**
 * To be able to open the file after its creation, we have to know, whether a feature or
 * unit test was generated.
 */
class TestFileResolver(override val base: String) : CreatedFileResolver(base) {
    override fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String {
        val prefix = if (parameters.parameters.contains("--unit")) "Unit" else "Feature"

        return base + command.location + "/" + prefix + "/" + parameters.className + ".php"
    }
}

/**
 * If we right click on tests/Unit, we do not want to suggest "Unit/" as the initial input.
 * Instead, we want to remove this prefix and also add the --unit flag.
 */
class TestTargetResolver(
    override val directoryResolver: DirectoryResolver
) : TargetResolver(directoryResolver) {
    override fun suggestInitialInputFor(target: String?, projectBasePath: String): InitialInputSuggestion {
        val defaultSuggestion = super.suggestInitialInputFor(target, projectBasePath)

        for (testType in listOf("Unit", "Feature")) {
            if (defaultSuggestion.name.startsWith(testType)) {
                // This slightly breaks, if you create e.g tests/Unit/Feature, but this should be a very
                // niche use case, so we will ignore it in order for the code to be more simple
                return InitialInputSuggestion(
                    defaultSuggestion.name.removePrefix("$testType/"),
                    if (testType == "Unit") listOf("--unit") else emptyList()
                )
            }
        }

        return defaultSuggestion
    }
}

class MakeTestActionExecution(
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
        get() = TestFileResolver(laravelProject.paths.base)

    override val targetResolver: TargetResolver
        get() = TestTargetResolver(directoryResolver)
}

class MakeTestAction : ArtisanMakeSubCommandAction(
    SubCommand("test", LaravelProjectPaths.TESTS)
) {
    override fun buildExecution(
        meta: SubCommand,
        project: Project,
        laravelProject: LaravelProject,
        targetFilePath: String?
    ): ArtisanMakeSubCommandActionExecution {
        return MakeTestActionExecution(meta, project, laravelProject, targetFilePath)
    }
}
