package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php

import com.intellij.notification.NotificationType
import com.github.niclasvaneyk.laravelmake.common.macos.docker.DockerForMac
import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.ProjectBasedNotifier
import com.github.niclasvaneyk.laravelmake.common.php.run.DockerNotStartedException
import com.github.niclasvaneyk.laravelmake.common.php.run.InterpreterScriptRunException
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions.OpenPHPInterpreterSettingsAction
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions.StartDockerOnMacAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication

class PHPInterpreterValidator(private val project: LaravelApplication) {
    fun validate(): ValidationResult {
        val notifier = ProjectBasedNotifier(project.project)

        try {
            val result = project.artisan.command(
                "",
                parameters = listOf("--version"),
            )

            if (result.wasFailure) {
                throw Exception(
                    "Could not execute `php artisan version`!\n" + result.log
                )
            }

            return ValidationResult(notifier, true)
        } catch (exception: Throwable) {
            return ValidationResult(notifier, false, exception)
        }
    }

    class ValidationResult(
        private val notifier: ProjectBasedNotifier,
        val isValid: Boolean,
        private val exception: Throwable? = null,
    ) {
        fun showNotification() {
            val notification = notifier.notification {
                it.title = "Could not use artisan binary"
                it.content = "${exception?.localizedMessage}\n\n"
                    .plus("Usually this means that you need to fix or")
                    .plus(" specify your PHP project interpreter ")
                    .plus("through the settings menu.")
                it.type = NotificationType.WARNING
                it.usePluginIcon()

                return@notification it
            }

            if (exception is DockerNotStartedException && DockerForMac.isAvailable()) {
                notification.addAction(StartDockerOnMacAction())
            } else if (exception is InterpreterScriptRunException) {
                notification.addAction(OpenPHPInterpreterSettingsAction())
            }

            notification.isImportant = true

            // TODO: Be more helpful / specific by adding actions or a link to
            //       the GH wiki, or the interpreter settings
            notifier.display(notification)
        }
    }
}
