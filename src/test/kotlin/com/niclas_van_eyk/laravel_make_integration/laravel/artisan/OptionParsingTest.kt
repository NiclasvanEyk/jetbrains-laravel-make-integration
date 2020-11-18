package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OptionParsingTest {
    companion object {
        val SAMPLE_OUTPUT = """
Description:
  Create a new custom Eloquent cast class

Usage:
  make:cast <name>

Arguments:
  name                  The name of the class

Options:
  -h, --help            Display this help message
  -q, --quiet           Do not output any message
  -V, --version         Display this application version
      --ansi            Force ANSI output
      --no-ansi         Disable ANSI output
  -n, --no-interaction  Do not ask any interactive question
      --env[=ENV]       The environment the command should run under
  -v|vv|vvv, --verbose  Increase the verbosity of messages: 1 for normal output, 2 for more verbose output and 3 for debug
        """
    }

    lateinit var parsed: Command

    @BeforeEach
    fun setUp() {
        parsed = parseArtisanCommandFromHelp(SAMPLE_OUTPUT, "test")
    }

    @Test
    fun it_parses_the_description() {
        assertEquals("Create a new custom Eloquent cast class", parsed.description)
    }

    @Test
    fun it_parses_the_arguments() {
        assertEquals("Create a new custom Eloquent cast class", parsed.description)
    }

    @Test
    fun it_parses_the_options() {
        val options = parsed.options

        val help = options[0]
        assertEquals("-h", help.shortForm)
        assertEquals("--help", help.name)
        assertEquals("Display this help message", help.description)
        assertEquals(OptionType.Flag, help.type)
    }
}