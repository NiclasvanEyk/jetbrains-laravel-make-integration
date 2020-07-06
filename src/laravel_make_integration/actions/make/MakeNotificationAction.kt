package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeNotificationAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Notifications/"
    }

    override fun subCommand(): String {
        return "notification"
    }
}