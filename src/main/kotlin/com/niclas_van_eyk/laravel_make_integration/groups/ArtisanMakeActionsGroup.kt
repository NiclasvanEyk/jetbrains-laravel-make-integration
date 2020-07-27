package com.niclas_van_eyk.laravel_make_integration.groups

import com.intellij.ide.actions.NonEmptyActionGroup
import com.intellij.openapi.actionSystem.*
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandAction
import com.niclas_van_eyk.laravel_make_integration.actions.make.*
import com.niclas_van_eyk.laravel_make_integration.services.LaravelMakeIntegrationProjectService

/**
 * This is the group of Actions for the File > New menu.
 *
 * This filters the Actions based on the right-clicked folder.
 * @see ArtisanMakeSubCommandAction.isFromContextMenu
 */
class ArtisanMakeActionsGroup: NonEmptyActionGroup() {
    private fun computeActions(): Array<ArtisanMakeSubCommandAction> {
        val actions = arrayOf(
            MakeCastAction(),
            MakeChannelAction(),
            MakeCommandAction(),
            MakeComponentAction(),
            MakeControllerAction(),
            MakeEventAction(),
            MakeExceptionAction(),
            MakeFactoryAction(),
            MakeJobAction(),
            MakeListenerAction(),
            MakeMailAction(),
            MakeMiddlewareAction(),
            MakeMigrationAction(),
            MakeModelAction(),
            MakeNotificationAction(),
            MakeObserverAction(),
            MakePolicyAction(),
            MakeProviderAction(),
            MakeRequestAction(),
            MakeResourceAction(),
            MakeRuleAction(),
            MakeSeederAction()
        )

        // These are the actions for the New context menu, so we will filter
        // the actions based on the folder that gets right clicked
        actions.forEach { it.isFromContextMenu = true }

        return actions
    }

    override fun update(e: AnActionEvent) {
        // Per default we remove all children, because we do not want to show
        // the group in a non-Laravel project
        this.removeAll()
        super.update(e)

        val project = e.project ?: return
        val service = project.getService(LaravelMakeIntegrationProjectService::class.java)

        if (service.isLaravelProject) {
            // But if we could find an Artisan binary, we re-add all the actions
            this.addAll(this.computeActions().toMutableList())
            // and set our visibility accordingly
            super.update(e)
        }
    }
}