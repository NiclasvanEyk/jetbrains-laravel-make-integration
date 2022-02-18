package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vcs.ui.FontUtil
import com.intellij.ui.BrowserHyperlinkListener
import com.intellij.ui.ColorUtil
import com.intellij.util.ui.JBHtmlEditorKit
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.StartupUiUtil
import com.intellij.util.ui.UIUtil
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.jetbrains.annotations.Nls
import java.io.IOException
import java.io.StringWriter
import javax.swing.JEditorPane
import javax.swing.border.EmptyBorder
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultCaret
import javax.swing.text.EditorKit
import javax.swing.text.html.HTMLDocument
import javax.swing.text.html.StyleSheet
import kotlin.String

/**
 * Copied and adjusted from [com.intellij.util.ui.HtmlPanel].
 */
class HtmlPanel: JEditorPane(UIUtil.HTML_MIME, ""), HyperlinkListener {
    init {
        isEditable = false
        isOpaque = false
        putClientProperty(HONOR_DISPLAY_PROPERTIES, true)
        addHyperlinkListener(this)
        editorKit = buildEditorKit()
        border = EmptyBorder(0, 10, 0, 0)
    }

    private fun buildEditorKit(): EditorKit {
        val htmlEditorKit = JBHtmlEditorKit(false)
        customizeStyleSheet(htmlEditorKit.styleSheet)

        return htmlEditorKit
    }

    private fun customizeStyleSheet(styles: StyleSheet) {
        styles.addStyleSheet(
            StartupUiUtil.createStyleSheet("""
                a { 
                    color: $linkColor; 
                    text-decoration: none;
                }
                
                body { 
                    padding-left: 1rem; 
                } 
            """.trimIndent())
        )
    }

    private val bodyFont get() = FontUtil.getCommitMessageFont()
    private val linkColor get() = "#" + ColorUtil.toHex(
        JBUI.CurrentTheme.Link.Foreground.ENABLED
    )

    override fun hyperlinkUpdate(e: HyperlinkEvent?) {
        BrowserHyperlinkListener.INSTANCE.hyperlinkUpdate(e)
    }

    override fun getSelectedText(): String? {
        val doc = document
        val start = selectionStart
        val end = selectionEnd
        try {
            val p0 = doc.createPosition(start)
            val p1 = doc.createPosition(end)
            val sw = StringWriter(p1.offset - p0.offset)
            editorKit.write(sw, doc, p0.offset, p1.offset - p0.offset)
            return StringUtil.removeHtmlTags(sw.toString())
        } catch (ignored: BadLocationException) {
        } catch (ignored: IOException) {
        }
        return super.getSelectedText()
    }

    override fun updateUI() {
        super.updateUI()
        val caret = caret as DefaultCaret
        caret.updatePolicy = DefaultCaret.NEVER_UPDATE
        if (document is HTMLDocument) {
            val foo = "bar"
        }
        update()
    }

    private fun update() {
        setBody(getBody())
        isVisible = content != null
        revalidate()
        repaint()
    }

    var content: String? = ""
        set(value) {
            field = value
            update()
        }

    private fun getBody() = content ?: ""

    private fun setBody(@Nls text: String) {
        if (text.isEmpty()) {
            setText("")
        } else {
            val html = htmlTemplate(text)
            setText(html)
        }
    }

    private fun htmlTemplate(bodyContent: String): String {
        return createHTML().html {
            head {
                style {
                    unsafe {
                        raw(defaultStyles())
                    }
                }
            }
            body { unsafe { raw(bodyContent) } }
        }
    }

    private fun defaultStyles(): String {
        return UIUtil
            .getCssFontDeclaration(bodyFont)
            .removePrefix("<style>")
            .removeSuffix("</style>")
    }
}
