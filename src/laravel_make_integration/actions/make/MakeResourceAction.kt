package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeResourceAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Http/Resources/"
    }

    override fun subCommand(): String {
        return "resource"
    }
}