package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeCastAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Casts/"
    }

    override fun subCommand(): String {
        return "cast"
    }
}