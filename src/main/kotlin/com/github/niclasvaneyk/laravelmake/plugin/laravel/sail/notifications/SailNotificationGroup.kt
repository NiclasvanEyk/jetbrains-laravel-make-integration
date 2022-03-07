package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail

import com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications.NotificationGroup
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

val SailNotificationGroup = object: NotificationGroup("Laravel Sail") {
    override fun notification(type: NotificationType, title: String, content: String): Notification {
        return super.notification(type, title, content).apply {
            icon = LaravelIcons.Sail
        }
    }
}