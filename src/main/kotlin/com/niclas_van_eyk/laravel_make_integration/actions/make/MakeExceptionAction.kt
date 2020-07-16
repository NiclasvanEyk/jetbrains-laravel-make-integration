package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeExceptionAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Exceptions/"
    }

    override fun subCommand(): String {
        return "exception"
    }
}