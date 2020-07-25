package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class LaravelProjectPaths(private val _base: String) {
    companion object {
        const val APP = "/app"
            const val BROADCASTING = "$APP/Broadcasting"
                const val CHANNELS = "$BROADCASTING/Channels"
            const val CASTS = "$APP/Casts"
            const val CONSOLE = "$APP/Console"
                const val COMMANDS = "$CONSOLE/Commands"
            const val EVENTS = "$APP/Events"
            const val EXCEPTIONS = "$APP/Exceptions"
            const val HTTP = "$APP/Http"
                const val CONTROLLERS = "$HTTP/Controllers"
                const val MIDDLEWARE = "$HTTP/Middleware"
                const val REQUESTS = "$HTTP/Requests"
                const val RESOURCES = "$HTTP/Resources"
            const val JOBS = "$APP/Jobs"
            const val LISTENERS = "$APP/Listeners"
            const val MAILS = "$APP/Middleware"
            const val MODELS = "$APP/Models"
            const val NOTIFICATIONS = "$APP/Notifications"
            const val OBSERVERS = "$APP/Observers"
            const val POLICIES = "$APP/Policies"
            const val PROVIDERS = "$APP/Providers"
            const val RULES = "$APP/Rules"
            const val VIEW = "$APP/View"
                const val COMPONENTS = "$VIEW/Components"
        const val DATABASE = "/database"
            const val MIGRATIONS = "$DATABASE/migrations"
            const val FACTORIES = "$DATABASE/factories"
            const val SEEDS = "$DATABASE/seeds"
        const val ARTISAN = "/artisan"
    }

    /**
     * Returns the path from the project root or null,
     * if the given path is not inside the project.
     */
    fun fromProjectRoot(absoluteFilePath: String): String {
        return absoluteFilePath.replace(base, "")
    }

    fun path(relativePath: String): String {
        return "$base/$relativePath"
    }

    val base: String
        get() = _base
}