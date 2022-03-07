package com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

abstract class NotificationGroup(private val id: String) {
    fun error(content: String, title: String = ""): Notification {
        return notification(NotificationType.ERROR, title, content)
    }

    fun info(content: String, title: String = ""): Notification {
        return notification(NotificationType.INFORMATION, title, content)
    }

    fun warn(content: String, title: String = ""): Notification {
        return notification(NotificationType.WARNING, title, content)
    }

    protected open fun notification(type: NotificationType, title: String, content: String) = Notification(id, title, content, type)
}