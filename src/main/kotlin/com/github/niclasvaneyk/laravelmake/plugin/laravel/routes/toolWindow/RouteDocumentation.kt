package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.toolWindow

import com.intellij.openapi.editor.*
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.util.DocumentUtil
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpAttributesOwner
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.*
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel

class RouteDocumentation(private val project: Project): JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        border = BorderFactory.createEmptyBorder()
    }

    private var preview = EditorFactory
        .getInstance()
        .createViewer(EditorFactory.getInstance().createDocument(""), project)

    private val message = JBPanelWithEmptyText().withEmptyText(
        "Select a route to see more information"
    )

    init {
        add(preview.component)
        preview.component.isVisible = false

        add(message)
    }

    fun showPreview(route: IntrospectedRoute) {
        if (!(route is ControllerRoute)) {
            showMessage("Preview not available for closure based routes")
            return
        }

        val descriptor = getOpenFileDescriptor(route.method)

        this.message.isVisible = false
        this.preview.component.isVisible = true

        val document = FileDocumentManager.getInstance().getDocument(descriptor.file)

        if (document === null) {
            showMessage("Something went wrong while building the preview!")
            return
        }

        swapPreview(
            EditorFactory.getInstance().createEditor(
                document,
                project,
                descriptor.file,
                true,
            )
        )

        this.preview.caretModel.moveToOffset(descriptor.offset)
        this.preview.scrollingModel.scrollToCaret(ScrollType.CENTER)

        descriptor.navigateIn(this.preview)
    }

    private fun swapPreview(newEditor: Editor) {
        val settings = newEditor.settings
        settings.isLineMarkerAreaShown = true
        settings.isFoldingOutlineShown = false
        settings.additionalColumnsCount = 0
        settings.additionalLinesCount = 0
        settings.isAnimatedScrolling = false
        settings.isAutoCodeFoldingEnabled = false
        remove(this.preview.component)
        EditorFactory.getInstance().releaseEditor(this.preview)

        this.preview = newEditor
        add(this.preview.component)
    }

    fun showMessage(message: String? = null) {
        if (message != null) {
            this.message.withEmptyText(message)
        }

        this.message.isVisible = true
        this.preview.component.isVisible = false
    }

    private fun getOpenFileDescriptor(method: Method): OpenFileDescriptor {
        val centralCodePartLineNumber = preview.offsetToLogicalPosition(
            (method as PhpAttributesOwner).textOffset
        ).line

        return OpenFileDescriptor(
            project,
            method.containingClass!!.containingFile.virtualFile,
            centralCodePartLineNumber,
            0
        )
    }
}
