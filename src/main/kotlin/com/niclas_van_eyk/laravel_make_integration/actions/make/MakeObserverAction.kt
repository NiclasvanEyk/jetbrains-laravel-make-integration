package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeObserverAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Observers/"
    }

    override fun subCommand(): String {
        return "observer"
    }
}