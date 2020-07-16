package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeFactoryAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/database/factories/"
    }

    override fun subCommand(): String {
        return "factory"
    }
}