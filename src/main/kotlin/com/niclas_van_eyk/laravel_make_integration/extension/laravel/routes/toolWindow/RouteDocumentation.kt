package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.jetbrains.php.PhpIndex
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.HtmlPanel
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.ClassBasedRouteAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.ControllerMethodAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.InvocableControllerAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteAction
import javax.swing.JComponent

class RouteDocumentation {
    companion object {
        fun empty() = JBPanelWithEmptyText()
            .withEmptyText("Select a route to see more information")

        fun forRoute(route: RouteAction?, project: Project): JComponent {
            if (route !is ClassBasedRouteAction) return empty()

            val clazz = PhpIndex
                .getInstance(project)
                .getClassesByFQN(route.className)
                .firstOrNull() ?: return empty()

            val method = if (route is ControllerMethodAction) {
                route.methodName
            } else {
                InvocableControllerAction.INVOKE_METHOD_NAME
            }
            val documentedCodePart = clazz.findMethodByName(method) ?: clazz

            val panel = HtmlPanel()
            panel.content = LaravelRouteDocumentationProvider()
                .generateDoc(documentedCodePart)

            return JBScrollPane(panel).apply {
                border = SideBorder(JBColor.border(), SideBorder.NONE)
            }
        }
    }
}
