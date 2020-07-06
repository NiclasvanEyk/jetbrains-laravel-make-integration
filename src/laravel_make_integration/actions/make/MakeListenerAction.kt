package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeListenerAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Listeners/"
    }

    override fun subCommand(): String {
        return "listener"
    }
}