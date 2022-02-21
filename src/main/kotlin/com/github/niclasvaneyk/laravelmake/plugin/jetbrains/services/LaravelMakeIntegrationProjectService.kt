package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services

import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.ProjectBasedNotifier
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.php.InterpreterInference
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelProjectFactory
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.PHPInterpreterValidator
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions.OpenPHPInterpreterSettingsAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service.Level

@Service(Level.PROJECT)
class LaravelMakeIntegrationProjectService(project: Project) {
    private val projectFactory = LaravelProjectFactory(project)
    val laravelProject = projectFactory.build()
    val isLaravelProject: Boolean
        get() = laravelProject != null

    init {
        if (!isLaravelProject) {
            println(
                LaravelMakeIntegrationBundle.message(
                    "projectService",
                    "${project.name} is not a valid Laravel project: " +
                        projectFactory.errors.joinToString(", ")
                )
            )
        } else {
            laravelProject!!

            ProgressBarBuilder(project).indeterminate("Validate Project Interpreter") {
                val interpreter = InterpreterInference(project).inferInterpreter()
                if (interpreter == null) {
                    displayInterpreterSetupRequiredNotification(project)

                    return@indeterminate
                }

                val validator = PHPInterpreterValidator(laravelProject)
                val result = validator.validate()

                // TODO: Maybe we should have this functionality be triggered by an
                //       action?
                if (!result.isValid) {
                    result.showNotification()
                }
            }
        }
    }

    private fun displayInterpreterSetupRequiredNotification(project: Project) {
        val notifier = ProjectBasedNotifier(project)
        val notification = notifier.notification {
            it.title = "Project Interpreter Setup Required"
            it.content = "It appears that you did not configure any PHP interpreter.\n" +
                "Specify one in the settings dialog in order to use" +
                " all features of the Laravel Make extension."
            it.type = NotificationType.WARNING
            it.usePluginIcon()

            return@notification it
        }

        notification.addAction(OpenPHPInterpreterSettingsAction())
        notifier.display(notification)
    }
}
