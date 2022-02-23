package com.github.niclasvaneyk.laravelmake.plugin.laravel

import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.ProjectBasedNotifier
import com.github.niclasvaneyk.laravelmake.common.php.InterpreterInference
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.PHPInterpreterValidator
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.actions.OpenPHPInterpreterSettingsAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.concurrency.Promise
import org.jetbrains.concurrency.runAsync

fun validateProjectInterpreter(app: LaravelApplication): Promise<Boolean> {
    val project = app.project

    return runAsync {
        val interpreter = InterpreterInference(project).inferInterpreter()
        if (interpreter == null) {
            displayInterpreterSetupRequiredNotification(project)

            return@runAsync false
        }

        val validator = PHPInterpreterValidator(app)
        val result = validator.validate()

        // TODO: Maybe we should have this functionality be triggered by an
        //       action?
        if (!result.isValid) {
            result.showNotification()
        }

        return@runAsync result.isValid
    }
}

private fun displayInterpreterSetupRequiredNotification(project: Project) {
    val notifier = ProjectBasedNotifier(project)
    val notification = notifier.notification {
        it.title = LaravelMakeIntegrationBundle.message("noProjectInterpreterError.modal.title")
        it.content = LaravelMakeIntegrationBundle.message("noProjectInterpreterError.modal.body")
        it.type = NotificationType.WARNING
        it.usePluginIcon()

        return@notification it
    }

    notification.addAction(OpenPHPInterpreterSettingsAction())
    notification.isImportant = true
    notifier.display(notification)
}
