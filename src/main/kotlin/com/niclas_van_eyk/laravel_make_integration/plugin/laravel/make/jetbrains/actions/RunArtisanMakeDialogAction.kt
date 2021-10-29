package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.jetbrains.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.niclas_van_eyk.laravel_make_integration.plugin.jetbrains.LaravelIcons
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.jetbrains.groups.ArtisanMakeActionsGroup
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.jetbrains.groups.DescriptionMode

/**
 * Action for opening a modal from which the sub-actions can be run.
 *
 * See https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/pull/8
 */
class RunArtisanMakeDialogAction : DumbAwareAction(LaravelIcons.LaravelLogo) {
    init {
        templatePresentation.description = "Run artisan:make"
        templatePresentation.text = "Run artisan:make"
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
                Runnable { },
                10
            )
            .showCenteredInCurrentWindow(project)
    }
}
