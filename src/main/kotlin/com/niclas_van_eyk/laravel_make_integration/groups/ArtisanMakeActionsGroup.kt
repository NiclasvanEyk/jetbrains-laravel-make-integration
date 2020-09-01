package com.niclas_van_eyk.laravel_make_integration.groups

import com.intellij.ide.actions.NonEmptyActionGroup
import com.intellij.openapi.actionSystem.*
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandAction
import com.niclas_van_eyk.laravel_make_integration.actions.make.*
import com.niclas_van_eyk.laravel_make_integration.services.LaravelMakeIntegrationProjectService

enum class DescriptionMode {
    /**
     * The description of the actions in this group is set to the actual description
     * of the action.
     */
    Normal,

    /**
     * The description is set to the label of the action.
     *
     * This makes sense, if you want to use the JBPopupFactory to display
     * the group, but only want to show the label and not the full description.
     */
    Compact,
}

/**
 * This is the group of Actions for the File > New menu.
 *
 * This filters the Actions based on the right-clicked folder.
 * @see ArtisanMakeSubCommandAction.isFromContextMenu
 */
class ArtisanMakeActionsGroup(
        private val filterBasedOnSelection: Boolean = true,
        private val descriptionMode: DescriptionMode = DescriptionMode.Normal
): NonEmptyActionGroup() {
    companion object {
        val AVAILABLE_ACTIONS = arrayOf(
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
    }

    private fun computeActions(): Array<ArtisanMakeSubCommandAction> {
        val actions = AVAILABLE_ACTIONS

        // These are the actions for the New context menu, so we will filter
        // the actions based on the folder that gets right clicked
        actions.forEach {
            it.isFromContextMenu = filterBasedOnSelection

            if (descriptionMode === DescriptionMode.Compact) {
                it.templatePresentation.text = it.command.capitalized
            }
        }

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