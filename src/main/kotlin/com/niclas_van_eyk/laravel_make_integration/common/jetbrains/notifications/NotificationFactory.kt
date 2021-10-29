package com.niclas_van_eyk.laravel_make_integration.common.jetbrains.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.niclas_van_eyk.laravel_make_integration.plugin.jetbrains.LaravelIcons
import javax.swing.Icon

class NotificationFactory {
    var title: String? = null
    var subTitle: String? = null
    var content: String? = null
    var type = NotificationType.INFORMATION
    var icon: Icon? = null

    fun build(): Notification {
        val notification = Notification(
            "laravel_make_integration_notification",
            icon,
            title,
            subTitle,
            content,
            type,
            null,
        )

        reset()

        return notification
    }

    fun usePluginIcon() {
        icon = LaravelIcons.LaravelLogo
    }

    private fun reset() {
        this.title = null
        this.subTitle = null
        this.content = null
        this.type = NotificationType.INFORMATION
    }
}
