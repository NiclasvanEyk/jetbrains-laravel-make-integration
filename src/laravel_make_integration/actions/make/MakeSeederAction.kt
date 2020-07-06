package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeSeederAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/database/seeds/"
    }

    override fun subCommand(): String {
        return "seeder"
    }
}