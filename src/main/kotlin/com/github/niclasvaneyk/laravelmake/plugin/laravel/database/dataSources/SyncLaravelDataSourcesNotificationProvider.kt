package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.dataSources

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings.settings
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications

/**
 * Shows a banner when editing the `config/database.php`-file and no data
 * source has been created by the plugin.
 */
class SyncLaravelDataSourcesNotificationProvider
    : EditorNotifications.Provider<EditorNotificationPanel>(), DumbAware  {
    private val KEY = Key.create<EditorNotificationPanel>("AutoconfigureDefaultDatabaseSourceBannerProvider")
    override fun getKey(): Key<EditorNotificationPanel> = KEY

    private fun update(file: VirtualFile, project: Project) = EditorNotifications.getInstance(project).updateNotifications(file)

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project,
    ): EditorNotificationPanel? {
        if (!file.path.endsWith("config/database.php")) return null
        val laravel = project.laravel() ?: return null
        if (!project.settings.shouldDisplayDefaultDatabaseConnectionSyncBanner) return null
        val dataSourceExists = LaravelDataSourceAutoConfigurer.getManagedDataSource(project) != null

        return EditorNotificationPanel(fileEditor).apply {
            icon(LaravelIcons.LaravelLogo)
            text =
                if (!dataSourceExists) "The default database connection can be added to the 'Database' tool window."
                else "The default database connection can be synced with the 'Database' tool window."
            createActionLabel(if (!dataSourceExists) "Add" else "Sync") {
                SyncLaravelDataSourcesAction().run(laravel)
                update(file, project)
            }
            createActionLabel(LaravelMakeIntegrationBundle.message("notification.dont-ask-again")) {
                project.settings.shouldDisplayDefaultDatabaseConnectionSyncBanner = false
                update(file, project)
            }
        }
    }
}