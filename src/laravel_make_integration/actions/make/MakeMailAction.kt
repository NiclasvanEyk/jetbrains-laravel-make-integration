package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeMailAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Mail/"
    }

    override fun subCommand(): String {
        return "mail"
    }
}