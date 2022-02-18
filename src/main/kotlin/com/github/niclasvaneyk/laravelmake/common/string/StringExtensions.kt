package com.github.niclasvaneyk.laravelmake.common.string

/**
 * Similar to [String.lines], but for paragraphs, so blocks of text separated by
 * a blank new line.
 */
fun String.paragraphs(): Sequence<String> {
    return this.splitToSequence("\r\n\r\n", "\n\n", "\r\r")
}
