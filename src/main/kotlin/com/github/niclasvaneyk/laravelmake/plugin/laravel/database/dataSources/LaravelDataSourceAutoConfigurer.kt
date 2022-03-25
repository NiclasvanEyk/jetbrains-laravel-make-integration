package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.common.collection.humanReadableKeys
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.notifications.LaravelMakeNotificationGroup
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConfigurationFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConnection
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.LaravelDatabaseDriver
import com.intellij.database.dataSource.DatabaseDriver
import com.intellij.database.dataSource.DatabaseDriverManager
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.dataSource.SchemaControl.AUTOMATIC
import com.intellij.database.introspection.DBIntrospectionOptions.SourceLoading.USER_AND_SYSTEM_SOURCES
import com.intellij.database.util.DataSourceUtil
import com.intellij.notification.Notification
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.wm.ToolWindowManager

/**
 * Responsible for managing a [LocalDataSource], which connects to the default
 * database of the Laravel [application].
 *
 * In the future, we could also provide list of the available connections
 * detected in the application and let the user decide which one to create.
 */
class LaravelDataSourceAutoConfigurer(
    private val application: LaravelApplication,

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
        val defaultConnectionName = laravelDatabaseConfiguration.default
        val defaultConnection = laravelDatabaseConfiguration.defaultConnection
        if (defaultConnection == null) {
            val availableConnections = laravelDatabaseConfiguration.connections.humanReadableKeys

            return LaravelMakeNotificationGroup.error(
                title = "Invalid config/database.php",
                content = "Default connection '$defaultConnectionName' " +
                    "could not be found in available connections " +
                    "$availableConnections!"
            ).notify(application.project)
        }

        val dataSource = createDataSourceFromConnection(defaultConnection)

        invokeLater {
            managedDataSource?.also { dataSourceManager.removeDataSource(it) }

            dataSourceManager.addDataSource(dataSource)
            DataSourceUtil.performAutoSyncTask(application.project, dataSource)

            parentNotification?.expire()
            openDatabaseToolWindow()
            LaravelMakeNotificationGroup
                .info("Successfully added connection '$defaultConnectionName' to the Database tool window!")
                .notify(application.project)
            // Or just open the database tool window and focus the newly created schema?
        }
    }

    private fun openDatabaseToolWindow() {
        ToolWindowManager
            .getInstance(application.project)
            .getToolWindow("Database")
            ?.show()
    }

    private fun introspectConfiguration(): DatabaseConfigurationFile {
        return DatabaseConfigurationFile(
            default = "mysql",
            connections = mapOf(
                "mysql" to DatabaseConnection(
                    driver = LaravelDatabaseDriver.mysql,
                    host = "127.0.0.1",
                    port = "3306",
                    database = "example_app",
                    username = "sail",
                    password = "password",
                )
            )
        )
    }

    private val DatabaseConfigurationFile.defaultConnection get() = connections[default]

    private fun createDataSourceFromConnection(connection: DatabaseConnection): LocalDataSource {
        return connection.deriveDataSource().apply {
            name = "Laravel"
            comment = MANAGED_DATA_SOURCE_MARKER_COMMENT
//            username = connection.username
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
