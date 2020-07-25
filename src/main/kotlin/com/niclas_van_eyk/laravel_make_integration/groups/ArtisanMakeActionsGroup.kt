package com.niclas_van_eyk.laravel_make_integration.groups

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.vfs.LocalFileSystem
import com.niclas_van_eyk.laravel_make_integration.actions.ArtisanMakeSubCommandAction
import com.niclas_van_eyk.laravel_make_integration.actions.make.*
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject

class ArtisanMakeActionsGroup: ActionGroup() {
    override fun canBePerformed(context: DataContext): Boolean {
        val basePath = LangDataKeys.PROJECT.getData(context)?.basePath ?: return false
        val baseFile = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return false

        return LaravelProject(baseFile).artisan.exists
    }

    override fun getChildren(event: AnActionEvent?): Array<ArtisanMakeSubCommandAction> {
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
        actions.forEach { it.disableBasedOnLocation = true }

        return actions
    }
}