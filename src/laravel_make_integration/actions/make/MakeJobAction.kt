package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeJobAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/Jobs/"
    }

    override fun subCommand(): String {
        return "job"
    }
}