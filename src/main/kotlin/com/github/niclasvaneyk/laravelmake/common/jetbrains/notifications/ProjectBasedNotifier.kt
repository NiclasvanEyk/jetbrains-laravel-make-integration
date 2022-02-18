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
        subTitle: String?,
        content: String?,
    ): Notification {
        return notification {
            it.type = type
            it.title = title
            it.subTitle = subTitle
            it.content = content
            it
        }
    }

    fun info(title: String? = null, subTitle: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.INFORMATION, title, subTitle, content))
    }

    fun warning(title: String? = null, subTitle: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.WARNING, title, subTitle, content))
    }

    fun error(title: String? = null, subTitle: String? = null, content: String? = null) {
        display(simpleNotificationOfType(NotificationType.ERROR, title, subTitle, content))
    }

    fun display(notification: Notification) {
        Notifications.Bus.notify(notification, project)
    }
}
