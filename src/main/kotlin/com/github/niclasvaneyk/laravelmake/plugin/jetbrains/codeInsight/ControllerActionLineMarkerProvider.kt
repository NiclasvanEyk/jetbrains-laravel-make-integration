package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.codeInsight

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.Method
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
    // TODO: Replace with L and globe in lower right corner
    override fun getIcon() = LaravelIcons.LaravelLogo

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

        elements
            .filter { it.elementType == PhpTokenTypes.IDENTIFIER && it.parent is Method }
            .forEach { identifier: PsiElement ->
                val method = identifier.parent as Method
                val route = routesByMethod[method] ?: return

                result.add(ControllerActionLineMarker(identifier, route, icon))
            }
    }
}

private fun detectLaravelApplication(elements: MutableList<out PsiElement>): LaravelApplication? {
    if (elements.isEmpty()) return null
    val projectService = elements[0].project.service<LaravelMakeProjectService>()
    return projectService.application
}
