package laravel_make_integration.actions.make

import laravel_make_integration.actions.NamespacedArtisanMakeAction

class MakeModelAction: NamespacedArtisanMakeAction() {
    override fun defaultFolder(): String {
        return "/app/"
    }

    override fun subCommand(): String {
        return "model"
    }

    override fun getInitialInputValue(target: String): String {
        val suggested = super.getInitialInputValue(target)

        // If the project contains a /app/Models directory, it is likely that the user wants to store
        // all the models there. Therefore we will prefix the suggested path with Models/ if we detect
        // that this folder exists.
        if (suggested.isNotEmpty() && laravelProject.paths.hasFolder("Models")) {
            return "Models/$suggested"
        }

        // Otherwise we will just use the default implementation
        return suggested
    }

    // As the models are stored in /app by default, we should not just activate this action
    // whenever one right-clicks *somewhere* inside the app folder.
    // Otherwise this action gets activated, when you right click from /app/Http/Controllers,
    // which is not where one would expect a model.
    // An exception is made for the /app/Models directory, as this is a non-standard but commonly
    // used place for storing models.
    // Maybe this could be configured somehow.
    override fun shouldBeActivatedWhenRightClickedOn(relativePathFromProjectRoot: String): Boolean {
        return arrayOf("/app", "/app/Models").contains(relativePathFromProjectRoot)
    }
}