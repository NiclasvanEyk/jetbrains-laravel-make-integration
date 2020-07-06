package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeExceptionAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Exceptions/"
    }

    override fun subCommand(): String {
        return "exception"
    }
}