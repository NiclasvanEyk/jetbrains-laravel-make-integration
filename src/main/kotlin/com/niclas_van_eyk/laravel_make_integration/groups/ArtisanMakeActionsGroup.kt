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
        val DEFAULT_ACTIONS = arrayOf(
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
            MakeSeederAction(),
            MakeTestAction()
        )
    }

    private fun computeActions(): Array<ArtisanMakeSubCommandAction> {
        val actions = DEFAULT_ACTIONS

        // These are the actions for the New context menu, so we will filter
        // the actions based on the folder that gets right clicked
        actions.forEach(this@ArtisanMakeActionsGroup::setActionContext)

        return actions
    }

    private fun setActionContext(action: ArtisanMakeSubCommandAction) {
        action.isFromContextMenu = filterBasedOnSelection

        if (descriptionMode === DescriptionMode.Compact) {
            action.templatePresentation.text = action.command.capitalized
        }
    }

    override fun update(e: AnActionEvent) {
        // Per default we remove all children, because we do not want to show
        // the group in a non-Laravel project
        this.removeAll()
        super.update(e)

        val project = e.project ?: return
        val service = project.getService(LaravelMakeIntegrationProjectService::class.java)

        if (service.isLaravelProject) {
            val actions = this.computeActions().toMutableList()

            if (service.hasCommands) {
                val liveWireCommand = service.commands.commands.find {
                    command -> command.name.equals("make:livewire")
                }

                if (liveWireCommand != null) {
                    val liveWireAction = MakeLivewireAction()
                    setActionContext(liveWireAction)

                    actions.add(liveWireAction)
                }

                actions.sortBy { it.command.capitalized }
            }

            // But if we could find an Artisan binary, we re-add all the actions
            this.addAll(actions)

            // and set our visibility accordingly
            super.update(e)
        }
    }
}