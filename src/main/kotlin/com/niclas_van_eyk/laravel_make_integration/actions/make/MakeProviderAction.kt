package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeProviderAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Providers/"
    }

    override fun subCommand(): String {
        return "provider"
    }
}