package com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection

import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandRunInfo
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.LoadedState

class CommandIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
) : CommandBasedIntrospecter<List<Command>>(artisan, progressBar) {
    companion object {
        private val log = Logger.getInstance(CommandIntrospecter::class.java)
    }

    override val description = "Scanning Artisan commands"
    override val command = CommandRunInfo(
        "list",
        null,
        listOf("--format=json"),
    )
    var snapshot: List<Command> = emptyList()

    init {
        introspectionState
            .filter { it is LoadedState }
            .subscribe(
                { snapshot = (it as LoadedState).result },
                { log.error(it) }
            )
    }

    override fun onCommandOutput(output: String, publish: (result: List<Command>) -> Unit) {
        val commands = GsonBuilder()
            .create()
            .fromJson(output, LaravelConsoleApplication::class.java)
            .commands
            .sortedBy { it.name }

        publish(commands)
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
