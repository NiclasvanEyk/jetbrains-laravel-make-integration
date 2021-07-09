package com.niclas_van_eyk.laravel_make_integration.extension.laravel.make

import com.niclas_van_eyk.laravel_make_integration.common.laravel.ArtisanMakeParameters

/**
 * Defines the logic to locate the file created by the make command.
 */
open class CreatedFileResolver(protected open val base: String) {
    open fun getCreatedFilePath(command: SubCommand, parameters: ArtisanMakeParameters): String? {
        return base + command.location + "/" + parameters.className + ".php"
    }
}
