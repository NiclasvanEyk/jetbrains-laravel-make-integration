package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeInsight

import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.phpMethods
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import javax.swing.Icon

class ControllerActionLineMarker(
    element: PsiElement,
    private val route: IntrospectedRoute,
    icon: Icon,
): LineMarkerInfo<PsiElement>(
    element,
    element.textRange,
    icon,
    { tooltip(route) },
    null,
    GutterIconRenderer.Alignment.LEFT,
    { tooltip(route) },
) {
    companion object {
        fun tooltip(route: IntrospectedRoute): String {
            val middleware = route.middleware.joinToString(", ") { it.basename }
            return "${route.normalizedHttpMethod} <b>${route.path}</b> [$middleware]"
        }
    }

//    override fun createGutterRenderer(): GutterIconRenderer {
//        return object: LineMarkerGutterIconRenderer<PsiElement>(this) {
//            override fun getClickAction(): AnAction? {
//                print("Foo")
//                return super.getClickAction()
//            }
//        }
//    }
}

class ControllerActionLineMarkerProvider: LineMarkerProviderDescriptor() {
    override fun getName() = "Laravel controller action"
    override fun getIcon() = LaravelIcons.RouteForGutter

    override fun getLineMarkerInfo(element: PsiElement) = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        val application = detectLaravelApplication(elements) ?: return
        val routes = application.introspection.routeIntrospecter.snapshot ?: return
        val routesByMethod = routes
            .filterIsInstance<ControllerRoute>()
            .associateBy { it.method }

        elements.phpMethods().forEach { (identifier, method) ->
            result.add(ControllerActionLineMarker(
                identifier,
                routesByMethod[method] ?: return@forEach,
                icon,
            ))
        }
    }
}

private fun detectLaravelApplication(elements: MutableList<out PsiElement>): LaravelApplication? {
    if (elements.isEmpty()) return null
    val projectService = elements[0].project.service<LaravelMakeProjectService>()
    return projectService.application
}