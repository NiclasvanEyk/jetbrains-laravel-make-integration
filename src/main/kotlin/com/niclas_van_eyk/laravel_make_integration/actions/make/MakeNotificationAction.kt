package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeNotificationAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Notifications/"
    }

    override fun subCommand(): String {
        return "notification"
    }
}