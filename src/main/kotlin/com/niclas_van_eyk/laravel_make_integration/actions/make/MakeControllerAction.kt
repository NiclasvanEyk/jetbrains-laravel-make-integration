package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeControllerAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Http/Controllers/"
    }

    override fun subCommand(): String {
        return "controller"
    }
}