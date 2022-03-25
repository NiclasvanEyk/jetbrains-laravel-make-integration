package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration

enum class LaravelDatabaseDriver {
    pgsql,
    mysql,
    sqlite,
//    sqlsrv, not supported by this plugin atm, as I can't / don't want to test it.
}

data class DatabaseConnection(
    val driver: LaravelDatabaseDriver,
    val url: String?,
    val host: String?,
    val port: String?,
    val database: String?,
    val username: String?,
    val password: String?,
)

data class DatabaseConfigurationFile(
    val default: String,
    val connections: Map<String, DatabaseConnection>
)
