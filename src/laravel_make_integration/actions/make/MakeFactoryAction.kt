package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeFactoryAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/database/migrations/"
    }

    override fun subCommand(): String {
        return "factory"
    }
}