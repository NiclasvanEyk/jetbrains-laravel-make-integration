package com.niclas_van_eyk.laravel_make_integration.actions

import java.io.File

interface ExecutesArtisanMake {
    /**
     * The `artisan make:subCommand`, e.g `"controller"` for `artisan make:controller`.
     */
    val subCommand: String

    /**
     * Executes the subCommand in the target directory.
     *
     * @return The created File.
     */
    fun make(target: File, name: String, params: List<String>): File
}