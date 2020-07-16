package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeRequestAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Http/Requests/"
    }

    override fun subCommand(): String {
        return "request"
    }
}