package com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

class ProjectBasedNotifier(private val project: Project) {
    fun notification(
        builder: (notification: NotificationFactory) -> NotificationFactory,
    ): Notification {
        return builder(NotificationFactory()).build()
    }

    private fun simpleNotificationOfType(
        type: NotificationType,
        title: String?,
        content: String?,
    ): Notification {
        return notification {
            it.type = type
            it.title = title
            it.content = content
            it
        }
    }

    fun info(title: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.INFORMATION, title, content))
    }

    fun warning(title: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.WARNING, title, content))
    }

    fun error(title: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.ERROR, title, content))
    }

    fun display(notification: Notification) {
        Notifications.Bus.notify(notification, project)
    }
}
