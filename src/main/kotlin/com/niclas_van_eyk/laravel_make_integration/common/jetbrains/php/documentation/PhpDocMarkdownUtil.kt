package com.niclas_van_eyk.laravel_make_integration.common.jetbrains.php.documentation

import com.intellij.openapi.util.text.StringUtil
import org.markdown4j.Markdown4jProcessor
import java.io.IOException

class PhpDocMarkdownUtil {
    private val ourMarkdownProcessor = Markdown4jProcessor()
    private val substitutions: MutableMap<String, String> = mutableMapOf(
        "<pre><code>" to "<pre>",
        "</code></pre>" to "</pre>",
        "<em>" to "<i>",
        "</em>" to "</i>",
        "<strong>" to "<b>",
        "</strong>" to "</b>",
        ": //" to "://",
        "<br  />" to "",
    )

    fun markdown2Html(markdown: String): String {
        val normalizedMarkdown = normalizeMarkdown(markdown)
        val html = try {
            ourMarkdownProcessor.process(normalizedMarkdown)
        } catch (e: IOException) {
            return ""
        }
        return adjustHtml(html)
    }

    private fun normalizeMarkdown(text: String): String {
        val lines = text.lines()
        val processedLines: MutableList<String?> = mutableListOf()
        var isInCode = false
        var html: String
        var processedLine: String
        val var4: Iterator<*> = lines.iterator()
        while (var4.hasNext()) {
            html = var4.next() as String
            processedLine = StringUtil.trimTrailing(html)
            processedLine = StringUtil.trimStart(processedLine, " ")
            if (processedLine.contains("```")) {
                isInCode = !isInCode
            } else if (isInCode) {
                if (processedLine.startsWith("</code>")) {
                    processedLines.add("```")
                    processedLine = StringUtil.trimStart(processedLine, "</code>")
                    isInCode = false
                }
            } else {
                var codeStart = false
                if (processedLine.endsWith("<code>")) {
                    codeStart = true
                    processedLine = StringUtil.trimEnd(processedLine, "<code>")
                }
                processedLine = processedLine.replace("<pre>", "```").replace("</pre>", "```")
                    .replace("<code>", "``").replace("</code>", "``")
                if (codeStart) {
                    processedLines.add(processedLine)
                    processedLine = "```"
                    isInCode = true
                }
            }
            processedLines.add(processedLine)
        }

        return processedLines.joinToString("\n")
    }

    private fun adjustHtml(html: String): String {
        var adjustedHtml = html

        substitutions.entries.forEach { (key, replacement) ->
            adjustedHtml = html.replace(key, replacement)
        }

        return adjustedHtml.trim { it <= ' ' }
    }
}
