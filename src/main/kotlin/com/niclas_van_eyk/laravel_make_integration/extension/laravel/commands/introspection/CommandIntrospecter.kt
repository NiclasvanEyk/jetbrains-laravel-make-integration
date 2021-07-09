package com.niclas_van_eyk.laravel_make_integration.extension.laravel.commands.introspection

import com.google.gson.GsonBuilder
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.common.laravel.Artisan
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandBasedIntrospecter
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandRunInfo
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.LoadedState
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.RevalidatingState

class CommandIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
) : CommandBasedIntrospecter<List<Command>>(artisan, progressBar) {
    override val description = "Scanning Artisan commands"
    override val command = CommandRunInfo(
        "list",
        null,
        listOf("--format=json"),
    )
    var snapshot: List<Command> = emptyList()

    init {
        introspectionState.subscribe {
            if (it is LoadedState) {
                snapshot = it.result
            }
        }
    }

    override fun marshalResult(output: String): List<Command> {
        return GsonBuilder()
            .create()
            .fromJson(output, LaravelConsoleApplication::class.java)
            .commands
    }

    override fun prepareCommandOutput(output: String): String? {
        return super.prepareCommandOutput(output)
            // For some reason an empty argument list gets serialized
            // to an empty array instead of an empty object. Maybe
            // because of associative arrays in php are kinda the
            // same as JSON objects.
            ?.replace(Regex("\"options\":\\[]]"), "\"options\":{}")
            ?.replace(Regex("\"arguments\":\\[]]"), "\"arguments\":{}")
            // These are getting added by Docker I think
            ?.replace(Regex("\n"), "")
    }
}
