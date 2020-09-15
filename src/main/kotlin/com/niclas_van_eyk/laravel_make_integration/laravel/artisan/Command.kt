package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

enum class OptionType { String, Class, Integer, Float, Flag }

class Option(
        val name: String,
        val type: OptionType,
        val description: String,
        val shortForm: String?
)

class Command(
        val name: String,
        val description: String,
        val arguments: List<String>,
        val options: List<Option>
)

/**
 * Parses the existing "make:*"-commands from the artisan:list output.
 */
fun parseArtisanMakeCommandNames(output: String): List<String> {
    return output.lines()
            .filter { it.trimStart().startsWith("make:") }
            .map { it.trim().split(Regex("[\\p{javaWhitespace}\u00A0\u2007\u202F]+"))[0] }
}

fun parseArtisanCommandFromHelp(output: String, name: String): Command {
    // The artisan command --help output has the following form:
    // Description:
    //   A helpful description
    //
    // Usage:
    //   namespace:command <anArgument>
    //
    // Arguments:
    //   anArgument                  A helpful description
    //
    // Options:
    //   -shortForm, --LongForm            A helpful description
    val lines = output.trim().lines()

    val descriptionIndex = lines.indexOfFirst { it.trimStart().startsWith("Description") }
    val usageIndex = lines.indexOfFirst { it.trimStart().startsWith("Usage") }
    val argumentsIndex = lines.indexOfFirst { it.trimStart().startsWith("Arguments") }
    val optionsIndex = lines.indexOfFirst { it.trimStart().startsWith("Options") }

    val description = lines.subList(descriptionIndex + 1, usageIndex).joinToString("\n").trimIndent()
    val arguments = lines.subList(argumentsIndex + 1, optionsIndex).joinToString("\n").trimIndent()
    val options = lines.subList(optionsIndex + 1, lines.size).joinToString("\n").trimIndent()

    return Command(
            name,
            description,
            arguments.lines(),
            options.lines().map { parseOptionFromHelpLine(it) }
    )
}

/**
 * Parses a line from the artisan command --help output to a structured form.
 */
fun parseOptionFromHelpLine(line: String): Option {
    // We expect the line to be of the form:
    // -o, --option[=SOMETHING]            A description [default: "some default"]
    //
    // Many things are optional here!
    val sides = line.trim().split("  ")

    // The flags and their descriptions are separated by at least a double space, so we can safely
    // split those into two (if they exist).
    val left = sides[0]

    val hasShortForm = left.contains(",")
    val leftParts = left.split(",")
    val name = if (hasShortForm) leftParts[1].trim() else left.trim()
    val shortForm = if (hasShortForm) leftParts[0].trim() else null

    // If you cannot assign a value to the option, our extension should indicate that.
    // Options that can be assigned values will ALWAYS contain the "="-sign
    val isFlag = !left.contains("=")

    // The type "Flag" can be inferred directly, more precise types can be supplied
    // for known options, like the "--model"-flag of artisan make:observer.
    val type = if (isFlag) OptionType.Flag else OptionType.String

    // We could also extract the default here, but currently this is not that interesting
    var description = ""
    if (sides.size > 1) {
        val right = sides.subList(1, sides.size).joinToString("").trim()
        description = right
    }

    return Option(name, type, description, shortForm)
}
