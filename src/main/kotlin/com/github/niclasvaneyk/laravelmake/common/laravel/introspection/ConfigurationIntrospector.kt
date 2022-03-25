package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

import com.github.niclasvaneyk.laravelmake.common.php.run.ArbitraryPhpRunner
import com.github.niclasvaneyk.laravelmake.plugin.laravel.database.configuration.DatabaseConfigurationFile
import com.google.gson.GsonBuilder
import org.intellij.lang.annotations.Language

/**
 * Returns the evaluated contents of a config/something.php file.
 */
class ConfigurationIntrospector(
    private val phpRunner: ArbitraryPhpRunner,
) {
    fun database(): DatabaseConfigurationFile {
        val json = readConfigurationAsJson("database")

        return GsonBuilder()
            .create()
            .fromJson(json, DatabaseConfigurationFile::class.java)
    }

    private fun readConfigurationAsJson(key: String): String {
        val beginMarker = "LARAVEL_MAKE_OUTPUT_BEGIN";
        val endMarker = "LARAVEL_MAKE_OUTPUT_END";

        @Language("InjectablePHP")
        val php = """
            echo "$beginMarker";
            echo json_encode(config('$key'));
            echo "$endMarker";
        """.trimIndent()
        val execution = phpRunner.run(php)

        return execution
            .log
            .substringAfterLast(beginMarker)
            .substringBeforeLast(endMarker)
            .trim()
    }
}
