package com.github.niclasvaneyk.laravelmake.common.laravel.events

class Events {
    companion object {
        /**
         * Dispatched when a batch of migrations has run.
         */
        const val MIGRATIONS_ENDED = "Illuminate\\Database\\Events\\MigrationsEnded"
    }
}