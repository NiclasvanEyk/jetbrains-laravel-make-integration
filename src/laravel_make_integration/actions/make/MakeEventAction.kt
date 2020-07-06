package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeEventAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Events/"
    }

    override fun subCommand(): String {
        return "event"
    }
}