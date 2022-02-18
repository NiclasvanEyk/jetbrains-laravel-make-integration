package com.github.niclasvaneyk.laravelmake.common.jetbrains.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import javax.swing.Icon

class NotificationFactory {
    var title: String? = null
    var content: String? = null
    var type = NotificationType.INFORMATION
    var icon: Icon? = null

    fun build(): Notification {
        val notification =  Notification(
            "Laravel MAKE",
            title ?: "",
            content ?: "",
            type
        ).apply {
            icon = icon
        }

        reset()

        return notification
    }

    fun usePluginIcon() {
        icon = LaravelIcons.LaravelLogo
    }

    private fun reset() {
        this.title = null
        this.content = null
        this.type = NotificationType.INFORMATION
    }
}
