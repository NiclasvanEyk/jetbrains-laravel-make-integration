package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.notifications

import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.NotificationGroup
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

val LaravelMakeNotificationGroup = object: NotificationGroup("Laravel Make") {
    override fun notification(type: NotificationType, title: String, content: String): Notification {
        return super.notification(type, title, content).apply {
            icon = LaravelIcons.LaravelLogo
        }
    }
}
