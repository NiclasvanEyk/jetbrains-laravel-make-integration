package com.niclas_van_eyk.laravel_make_integration.filesystem

import com.niclas_van_eyk.laravel_make_integration.actions.SubCommand
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject

open class CreatedFileResolver(protected open val project: LaravelProject) {
    open fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String? {
        return project.paths.base + command.location + "/" + parameters.className + ".php"
    }
}