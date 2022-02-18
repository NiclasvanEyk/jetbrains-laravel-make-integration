package com.github.niclasvaneyk.laravelmake.common.laravel

/**
 * The conventions, so where to put files in a Laravel project.
 *
 * More precise where the artisan make commands place a file after creation.
 *
 * Can be looked up here: https://github.com/laravel/framework/tree/8.x/src/Illuminate/Foundation/Console
 */
class LaravelProjectPaths(val base: String) {
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
        const val LIVEWIRE = "$HTTP/Livewire"
        const val REQUESTS = "$HTTP/Requests"
        const val RESOURCES = "$HTTP/Resources"

        const val JOBS = "$APP/Jobs"

        const val LISTENERS = "$APP/Listeners"

        const val MAILS = "$APP/Mail"

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

        const val TESTS = "/tests"
        const val FEATURE_TESTS = "$TESTS/Feature"
        const val UNIT_TESTS = "$TESTS/Unit"

        const val ARTISAN = "/artisan"

        const val COMPOSER_LOCK = "/composer.lock"
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
}
