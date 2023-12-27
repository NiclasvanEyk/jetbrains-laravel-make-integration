package com.github.niclasvaneyk.laravelmake.common.string

/**
 * Similar to [String.lines], but for paragraphs, so blocks of text separated by
 * a blank new line.
 */
fun String.paragraphs(): Sequence<String> {
    return this.splitToSequence("\r\n\r\n", "\n\n", "\r\r")
}

fun String.containedJson(): String? {
    // Sometimes the json contains newlines, which mess with the
    // serialization, that e.g. the action of a route is suddenly null, even
    // though if we remove the newlines everything works fine.
    // This is kinda hacky, but works for routes.
    val output = this
        .lines()
        // Pretty hacky, but for some reason a proper regex does not work.
        // Tried it.matches("\\[.*\\] Xdebug".toRegex())), but as soon as \\] is
        // introduced, nothing matches anymore. Weird.
        .filter { !it.contains("] Xdebug:") }
        // Again, pretty hacky but docker sometimes prints such a message
        // while starting containers which trips up JSON parsing.
        .filter { !it.trim().startsWith("[+]") }
        .joinToString("")

    val beginOfJson = output.indexOfAny(listOf("{", "["))
    val endOfJson = output.lastIndexOfAny(listOf("]", "}"))

    if (beginOfJson == -1 || endOfJson == -1) return null

    return output.substring(beginOfJson, endOfJson + 1).trim()
}

fun String.makeForLaravelOutput(): String {
    return this
        .substringAfterLast("<MAKE_FOR_LARAVEL_OUTPUT:BEGIN>")
        .substringBefore("<MAKE_FOR_LARAVEL_OUTPUT:END>")
}