package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConfigurationFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConnection
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.LaravelDatabaseDriver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.database.dataSource.DatabaseDriver
import com.intellij.database.dataSource.DatabaseDriverManager
import com.intellij.openapi.project.Project
import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.dataSource.SchemaControl.AUTOMATIC
import com.intellij.database.introspection.DBIntrospectionOptions.SourceLoading.USER_AND_SYSTEM_SOURCES
import com.intellij.database.util.DataSourceUtil
import com.intellij.javascript.nodejs.execution.runExecutionUnderProgress
import com.intellij.notification.Notification
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.rd.util.withModalProgressContext

/**
 * Responsible for managing a [LocalDataSource], which connects to the default
 * database of the Laravel [application].
 *
 * In the future, we could also provide list of the available connections
 * detected in the application and let the user decide which one to create.
 */
class LaravelDataSourceAutoConfigurer(
    project: Project,

    /**
     * An optional notification that should be hidden after the action terminates.
     */
    private val parentNotification: Notification? = null,
) {
    companion object {
        /**
         * This comment marks the datasource as managed by Laravel Make.
         */
        const val MANAGED_DATA_SOURCE_MARKER_COMMENT = "Managed by Laravel Make."
    }

    private val application = project.laravel()!!
    private val dataSourceManager get() = LocalDataSourceManager.getInstance(application.project)

    /**
     * The [LocalDataSource] managed by this plugin.
     */
    private val managedDataSource: LocalDataSource? = dataSourceManager
        .dataSources
        .firstOrNull { it.comment == MANAGED_DATA_SOURCE_MARKER_COMMENT }

    /**
     * Updates the managed [LocalDataSource] based on the `database.default`
     * configuration in the Laravel [application].
     */
    fun updateManagedDataSource() {
        val laravelDatabaseConfiguration = introspectConfiguration()
        val defaultConnection = laravelDatabaseConfiguration.defaultConnection
        if (defaultConnection == null) {
            return
            // TODO: Display a notification stating that the app is likely misconfigured?
        }

        val dataSource = createDataSourceFromConnection(defaultConnection)

        invokeLater {
            managedDataSource?.also { dataSourceManager.removeDataSource(it) }

            dataSourceManager.addDataSource(dataSource)
            DataSourceUtil.performAutoSyncTask(application.project, dataSource)

            parentNotification?.expire()

            // Notification that everything went as planned?
            // Or just open the database tool window and focus the newly created schema?
        }
    }

    private fun introspectConfiguration(): DatabaseConfigurationFile {
        TODO("")
    }

    private val DatabaseConfigurationFile.defaultConnection get() = connections[default]

    private fun createDataSourceFromConnection(connection: DatabaseConnection): LocalDataSource {
        return connection.deriveDataSource().apply {
            name = "Laravel"
            comment = MANAGED_DATA_SOURCE_MARKER_COMMENT
            username = "db" // TODO: why? Might need some adjustments
            isConfiguredByUrl = true
            isAutoSynchronize = true
            setSourceLoading(USER_AND_SYSTEM_SOURCES)
            setCheckOutdated(true)
            setSchemaControl(AUTOMATIC)
        }
    }

    private fun DatabaseConnection.deriveDataSource(): LocalDataSource {
        return LocalDataSource.fromDriver(driver.jetbrainsDriver, jdbcUrl, false)
    }

    private val DatabaseConnection.jdbcUrl: String get() {
        return "jdbc:${driver.jdbcSchema}://$host:$port/?user=$username&password=$password"
    }

    private val LaravelDatabaseDriver.jetbrainsDriver: DatabaseDriver get() {
        val drivers = DatabaseDriverManager.getInstance()

        return when(this) {
            LaravelDatabaseDriver.mysql -> drivers.getDriver("mysql.8")
            LaravelDatabaseDriver.pgsql -> drivers.getDriver("postgresql")
            LaravelDatabaseDriver.sqlite -> TODO()
        }
    }

    private val LaravelDatabaseDriver.jdbcSchema: String get() {
        return when(this) {
            LaravelDatabaseDriver.mysql -> "mysql"
            LaravelDatabaseDriver.pgsql -> "postgresql"
            LaravelDatabaseDriver.sqlite -> "sqlite"
        }
    }
}
