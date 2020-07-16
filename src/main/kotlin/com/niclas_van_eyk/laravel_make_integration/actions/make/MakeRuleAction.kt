package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.niclas_van_eyk.laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeRuleAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Rules/"
    }

    override fun subCommand(): String {
        return "rule"
    }
}