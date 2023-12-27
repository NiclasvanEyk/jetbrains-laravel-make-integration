@file:Suppress("UnstableApiUsage")

package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeVision

import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeVision.RouteInlayHintsSettingsSnippetCollector.Companion.isSettingsSnippetEditor
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.ControllerRoute
import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.InlayPresentation
import com.intellij.codeInsight.hints.presentation.PresentationFactory
import com.intellij.openapi.editor.BlockInlayPriority
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.startOffset
import com.jetbrains.php.lang.psi.elements.Method
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * See [description] for a description of what this class does.
 */
class RouteInlayHintsProvider : InlayHintsProvider<NoSettings> {
    companion object {
        internal val KEY: SettingsKey<NoSettings> = SettingsKey("laravel-make.inlay-hints.controller-actions")
    }

    override val key = KEY
    override val name = "Laravel controller action methods"
    override val description = """
        Shows the HTTP method and path of a controller action above the method mapped to it. Currently only displays one path, if multiple paths point to a single action. 
    """.trimIndent()
    override val previewText = RouteInlayHintsSettingsSnippetCollector.SNIPPET

    override fun createSettings() = NoSettings()
    override fun createConfigurable(settings: NoSettings) = object: ImmediateConfigurable {
        override fun createComponent(listener: ChangeListener): JComponent = JPanel()
    }

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink,
    ): InlayHintsCollector? {
        if (isSettingsSnippetEditor(editor)) {
            return RouteInlayHintsSettingsSnippetCollector()
        }
        if (editor !is EditorImpl) return null
        val laravel = file.project.laravel() ?: return null
        val introspector = laravel.introspection.routeIntrospecter
        val routes = introspector.snapshot ?: return null

        val routesByMethod = routes
            .filterIsInstance<ControllerRoute>()
            .associateBy { it.fqn }

        return ControllerActionInlayHintsCollector(editor, routesByMethod)
    }
}

internal class ControllerActionInlayHintsCollector(
    private val editor: EditorImpl,
    private val routesByMethod: Map<String, ControllerRoute>,
    private val factory: PresentationFactory = PresentationFactory(editor),
) : InlayHintsCollector {
    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        if (element !is Method) return true
        val route = routesByMethod[element.fqn] ?: return true

        sink.addCodeVisionElement(editor, element.startOffset, 0, ControllerActionInlayHintPresentation(route, factory).build())
        return true
    }
}

internal class ControllerActionInlayHintPresentation(
    private val route: ControllerRoute,
    private val factory: PresentationFactory,
) {
    fun build(): InlayPresentation = factory.run {
        smallTextWithoutBackground("${route.normalizedHttpMethod} ${route.path}")
    }
}