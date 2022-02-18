package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains

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
