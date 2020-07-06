package laravel_make_integration.actions

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import laravel_make_integration.actions.make.*
import laravel_make_integration.laravel.Paths
import laravel_make_integration.laravel.Project
import laravel_make_integration.targetFileFromEvent

class LaravelActionGroup: ActionGroup() {
    override fun canBePerformed(context: DataContext): Boolean {
        val basePath = LangDataKeys.PROJECT.getData(context)?.basePath ?: return false
        val baseFile = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return false

        return Project(baseFile).artisan.exists
    }

    override fun getChildren(event: AnActionEvent?): Array<NamespacedArtisanMakeAction> {
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
        actions.forEach { it.doCheckForSubfolders = true }

        return actions
    }
}