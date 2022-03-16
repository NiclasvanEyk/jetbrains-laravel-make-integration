package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeInsight

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.intellij.codeInsight.daemon.LineMarkerInfo
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