package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeSeederAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/database/seeds/"
    }

    override fun subCommand(): String {
        return "seeder"
    }
}