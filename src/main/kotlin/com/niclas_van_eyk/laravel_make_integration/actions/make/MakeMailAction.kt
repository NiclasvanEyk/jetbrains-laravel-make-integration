package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeMailAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Mail/"
    }

    override fun subCommand(): String {
        return "mail"
    }
}