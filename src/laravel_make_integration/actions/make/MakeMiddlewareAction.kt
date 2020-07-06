package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeMiddlewareAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Http/Middleware/"
    }

    override fun subCommand(): String {
        return "middleware"
    }
}