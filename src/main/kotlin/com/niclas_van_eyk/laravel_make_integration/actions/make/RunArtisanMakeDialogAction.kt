package com.niclas_van_eyk.laravel_make_integration.actions.make

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.niclas_van_eyk.laravel_make_integration.LaravelIcons
import com.niclas_van_eyk.laravel_make_integration.groups.ArtisanMakeActionsGroup
import com.niclas_van_eyk.laravel_make_integration.groups.DescriptionMode

/**
 * Action for opening a modal from which the sub-actions can be run.
 */
class RunArtisanMakeDialogAction: DumbAwareAction(LaravelIcons.LaravelLogo) {
    init {
        templatePresentation.description = "Run artisan:make"
        templatePresentation.text        = "Run artisan:make"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        JBPopupFactory
                .getInstance()
                .createActionGroupPopup(
                        "artisan make",
                        ArtisanMakeActionsGroup(
                                false,
                                DescriptionMode.Compact
                        ),
                        e.dataContext,
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                        false,
                        Runnable {  },
                        10
                )
                .showCenteredInCurrentWindow(project)
    }
}