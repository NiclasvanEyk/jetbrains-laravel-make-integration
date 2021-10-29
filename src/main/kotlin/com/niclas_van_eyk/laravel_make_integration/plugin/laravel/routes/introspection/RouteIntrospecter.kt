package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection

import com.google.gson.GsonBuilder
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.common.laravel.Artisan
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandRunInfo
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandBasedIntrospecter

class RouteIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
) : CommandBasedIntrospecter<List<RouteListEntry>>(artisan, progressBar) {
    override val description = "Scanning Laravel routes"
    override val command = CommandRunInfo("route", "list", listOf("--json"))

    override fun marshalResult(output: String): List<RouteListEntry> {
        // Sometimes the json contains newlines, which mess with the
        // serialization, that e.g. the action of a route is suddenly null, even
        // though if we remove the newlines everything works fine.
        // This is kinda hacky, but works for routes.
        val sanitizedOutput = output.replace("\n", "")

        val routes = GsonBuilder()
            .create()
            .fromJson(sanitizedOutput, Array<RouteListEntry>::class.java)

        return routes.toList()
    }
}
