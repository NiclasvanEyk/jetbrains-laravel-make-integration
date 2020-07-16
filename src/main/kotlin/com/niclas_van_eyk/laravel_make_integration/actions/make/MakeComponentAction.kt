package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeComponentAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/View/Components/"
    }

    override fun subCommand(): String {
        return "component"
    }
}