@file:Suppress("UnstableApiUsage")

package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeVision

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.BasicRouteInformation
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteOrigin
import com.intellij.codeInsight.hints.InlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.codeInsight.hints.addCodeVisionElement
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.openapi.editor.BlockInlayPriority
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.startOffset
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

internal class RouteInlayHintsSettingsSnippetCollector() : InlayHintsCollector {
    companion object {
        val SNIPPET = """
            <?php
            
            class PostsController 
            {
                public function index() 
                {
                    // ...
                }
            }
        """.trimIndent()

        fun isSettingsSnippetEditor(editor: Editor): Boolean {
            return editor.document.text === SNIPPET && editor.isViewer
        }
    }

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        if (element !is Method) return true
        val route = ControllerRoute(BasicRouteInformation(
            path = "/posts",
            httpMethod = "GET",
            name = "posts.index",
            middleware = emptyList(),
        ), RouteOrigin.PROJECT, element.parent as PhpClass, element, null)
        val factory = PresentationFactory(editor as EditorImpl)

        sink.addCodeVisionElement(
            editor,
            element.startOffset,
            0,
            ControllerActionInlayHintPresentation(route, factory).build(),
        )
        return false
    }
}