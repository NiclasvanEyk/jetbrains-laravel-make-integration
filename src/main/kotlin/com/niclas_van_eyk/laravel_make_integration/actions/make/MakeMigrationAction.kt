package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeMigrationAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/database/migrations/"
    }

    override fun subCommand(): String {
        return "migration"
    }
}