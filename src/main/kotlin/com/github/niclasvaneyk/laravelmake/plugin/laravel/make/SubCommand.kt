package com.github.niclasvaneyk.laravelmake.plugin.laravel.make

import java.util.*

open class SubCommand(
    /**
     * The artisan sub-command, e.g `controller` for `artisan make:controller`.
     */
    val command: String,

    /**
     * The location relative from the project root where this class gets generated.
     */
    open val location: String
) {
    val capitalized: String get() = command.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val description: String get() = "Create a new Laravel $capitalized"
    val asArtisanCommand: String get() = "make:$command"
}
