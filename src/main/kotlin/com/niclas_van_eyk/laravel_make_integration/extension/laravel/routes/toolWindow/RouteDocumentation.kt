package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow

import com.intellij.codeInsight.documentation.DocumentationComponent
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanelWithEmptyText
import com.jetbrains.php.PhpIndex
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

            return DocumentationComponent.createAndFetch(project, documentedCodePart) {}
        }
    }
}
