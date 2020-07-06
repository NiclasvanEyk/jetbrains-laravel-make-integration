package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeChannelAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Broadcasting/"
    }

    override fun subCommand(): String {
        return "channel"
    }
}