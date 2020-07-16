package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakePolicyAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Policies/"
    }

    override fun subCommand(): String {
        return "policy"
    }
}