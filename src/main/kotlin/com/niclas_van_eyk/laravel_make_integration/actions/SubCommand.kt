package com.niclas_van_eyk.laravel_make_integration.actions

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
    val capitalized: String get() = command.capitalize()
    val description: String get() = "Create a new Laravel $capitalized"
    val asArtisanCommand: String get() = "make:$command"
}