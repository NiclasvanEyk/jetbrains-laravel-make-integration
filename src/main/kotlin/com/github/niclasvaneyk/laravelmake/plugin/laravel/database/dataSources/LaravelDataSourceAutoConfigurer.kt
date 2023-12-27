package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.common.collection.humanReadableKeys
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.ConfigurationIntrospector
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.notifications.LaravelMakeNotificationGroup
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConfigurationFile
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConnection
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.LaravelDatabaseDriver
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SAIL_DATABASE_SERVICE_NAMES
import com.intellij.database.dataSource.DatabaseDriver
import com.intellij.database.dataSource.DatabaseDriverManager
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.dataSource.SchemaControl.AUTOMATIC
import com.intellij.database.introspection.DBIntrospectionOptions.SourceLoading.USER_AND_SYSTEM_SOURCES
import com.intellij.database.util.DataSourceUtil
import com.intellij.docker.agent.util.findDockerComposeConfigurationFiles
import com.intellij.docker.remote.compose.DockerComposeProjectService
import com.intellij.notification.Notification
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.progress.runModalTask
import com.intellij.openapi.project.Project
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

    private val configurationIntrospector: ConfigurationIntrospector,

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

        /**
         * The [LocalDataSource] managed by this plugin.
         */
        fun getManagedDataSource(project: Project): LocalDataSource? = LocalDataSourceManager
            .getInstance(project)
            .dataSources
            .firstOrNull { it.comment == MANAGED_DATA_SOURCE_MARKER_COMMENT }
    }

    /**
     * Updates the managed [LocalDataSource] based on the `database.default`
     * configuration in the Laravel [application].
     */
    fun updateManagedDataSource() {
        runModalTask("Introspecting Database Configuration...", application.project) {
            // This will likely take up almost all the waiting time, so
            // the title of the task only describes this part, and we only
            // need to check for cancellation after this.
            // The remaining code runs <1s, so no need to do any checks /
            // progress bars there.
            val laravelDatabaseConfiguration = introspectConfiguration()
            it.checkCanceled()

            val defaultConnectionName = laravelDatabaseConfiguration.default
            val defaultConnection = laravelDatabaseConfiguration.defaultConnection
            if (defaultConnection == null) {
                val availableConnections = laravelDatabaseConfiguration.connections.humanReadableKeys

                return@runModalTask LaravelMakeNotificationGroup.error(
                    title = "Invalid config/database.php",
                    content = "Default connection '$defaultConnectionName' " +
                            "could not be found in available connections " +
                            "$availableConnections!"
                ).notify(application.project)
            }

            val dataSource = createDataSourceFromConnection(defaultConnection)

            invokeLater {
                val dataSourceManager = LocalDataSourceManager.getInstance(application.project)
                getManagedDataSource(application.project)?.also { dataSourceManager.removeDataSource(it) }

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
    }

    private fun openDatabaseToolWindow() {
        ToolWindowManager
            .getInstance(application.project)
            .getToolWindow("Database")
            ?.show()
    }

    private fun introspectConfiguration(): DatabaseConfigurationFile {
        return configurationIntrospector.database()
    }

    private val DatabaseConfigurationFile.defaultConnection get() = connections[default]

    private fun createDataSourceFromConnection(connection: DatabaseConnection): LocalDataSource {
        return connection.deriveDataSource().apply {
            name = "Laravel"
            comment = MANAGED_DATA_SOURCE_MARKER_COMMENT
//            username = connection.username
//            isConfiguredByUrl = true
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
        val host =
            // When using Laravel Sail, the host is configured as the
            // service name, as this gets resolved via DNS by Docker
            // to the right container. However, we want to connect from
            // the host machine to the database, and therefore cannot
            // just address the container via its service name.
            // We just assume the user is using Sail in this case and
            // expect the container to be bridged to localhost.
            if (SAIL_DATABASE_SERVICE_NAMES.contains(host)) "localhost"
            else host

        return "jdbc:${driver.jdbcSchema}://$host:$port/$database?user=$username&password=$password"
    }

    private val LaravelDatabaseDriver.jetbrainsDriver: DatabaseDriver get() {
        val drivers = DatabaseDriverManager.getInstance()

        return when(this) {
            LaravelDatabaseDriver.mysql -> drivers.getDriver("mysql.8")
            LaravelDatabaseDriver.pgsql -> drivers.getDriver("postgresql")
            LaravelDatabaseDriver.sqlite -> drivers.getDriver("sqlite3")
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
