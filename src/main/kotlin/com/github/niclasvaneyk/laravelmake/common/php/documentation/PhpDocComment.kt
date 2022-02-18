package com.github.niclasvaneyk.laravelmake.common.php.documentation

import com.intellij.util.containers.nullize

class PhpDocComment(rawText: String) {
    val summary: String?
    val description: String?

    init {
        val lines = splitIntoLines(rawText)
        val (textLines, tagLines) = bucketByLineType(lines)
        summary = textLines.getOrNull(0)
        description = textLines
            .drop(1)
            .nullize()
            ?.dropWhile { it.isBlank() }
            ?.joinToString("\n")
    }

    private fun splitIntoLines(raw: String): List<String> {
        return raw
            .lines()
            .map {
                it
                    .trim()
                    .removeSuffix("*/")
                    .removePrefix("/**")
                    .removePrefix("*")
                    .trim()
            }
            .dropWhile { it.isBlank() }
            .dropLastWhile { it.isBlank() }
    }

    private fun bucketByLineType(lines: List<String>): Pair<List<String>, List<String>> {
        val firstTagIndex = lines.indexOfFirst { it.startsWith("@") }
        val textualLines: List<String>
        val tagLines: List<String>

        if (firstTagIndex == -1) {
            textualLines = lines
            tagLines = emptyList()
        } else {
            textualLines = lines.subList(0, firstTagIndex)
            tagLines = lines.subList(firstTagIndex, lines.size)
        }

        return Pair(textualLines, tagLines)
    }
}
