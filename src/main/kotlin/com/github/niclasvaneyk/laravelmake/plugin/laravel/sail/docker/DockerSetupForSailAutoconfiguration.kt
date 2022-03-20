package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.docker

import com.github.niclasvaneyk.laravelmake.common.jetbrains.messageBus.listenOnce
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.AutoconfigureLaravelSailAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailNotificationGroup
import com.intellij.docker.DockerCloudType
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.ide.SaveAndSyncHandlerListener
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServersManager

/**
 * In order for this plugin to work properly, the user has to set up their
 * local Docker environment.
 *
 * This service is responsible for ensuring this is indeed the case, and
 * displays useful popup notifications that point the user to the right settings
 * otherwise.
 */
@Service(PROJECT)
class DockerSetupForSailAutoconfiguration(private val project: Project) {
    /**
     * Whether we have at least one Docker server that we can work with.
     *
     * Otherwise, all the things that [AutoconfigureLaravelSailAction] will
     * do won't work!
     */
    val hasBeenCompleted get() = RemoteServersManager
        .getInstance()
        .getServers(DockerCloudType.getInstance())
        .isNotEmpty()

    fun begin() {
        setupDockerServerChangeListener()
        promptUserToConfigureDockerServer()
    }

    /**
     * Once the user has configured docker, we should again display a
     * prompt to configure Laravel Sail, since this was the original intent.
     */
    @Suppress("UnstableApiUsage")
    private fun setupDockerServerChangeListener() {
        project.listenOnce(SaveAndSyncHandlerListener.TOPIC) { unsubscribe ->
            object : SaveAndSyncHandlerListener {
                override fun beforeRefresh() = Unit
                override fun beforeSave(task: SaveAndSyncHandler.SaveTask, forceExecuteImmediately: Boolean) {
                    // We just assume, that the user has successfully set up the connection
                    // and therefore reliably show the Sail Autoconfiguration again.
                    AutoconfigureLaravelSailAction.notification().notify(project)
                    unsubscribe()
                }
            }
        }
    }

    /**
     * Opens the right settings for the user.
     */
    private fun promptUserToConfigureDockerServer() {
        SailNotificationGroup
            .info(
                title = "Docker Setup Required!",
                content = "Just click the plus icon in the Docker settings and create an entry for your local setup. The one created by default should work for most setups. Afterwards I'll prompt you again to configure Sail.",
            )
            .addAction(object : DumbAwareAction("Open Docker Settings") {
                override fun actionPerformed(e: AnActionEvent) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, "Docker")
                }
            })
            .notify(project)
    }
}