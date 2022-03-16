package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.IntrospectionList
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandRunInfo
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CouldNotExtractJsonException
import com.github.niclasvaneyk.laravelmake.common.string.containedJson
import com.google.gson.GsonBuilder
import com.intellij.openapi.application.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.NonUrgentExecutor
import com.jetbrains.php.PhpIndex
import java.util.concurrent.Callable

class RouteIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    private val project: Project,
) : CommandBasedIntrospecter<List<IntrospectedRoute>>(artisan, progressBar) {
    override val description = "Scanning Laravel routes"
    override val command = CommandRunInfo("route", "list", listOf("--json"))

    companion object {
        protected val log = Logger.getInstance(IntrospectionList::class.java)
    }

    override fun onCommandOutput(output: String, publish: (result: List<IntrospectedRoute>) -> Unit) {
        val routes: List<RouteListEntry>
        try {
            routes = GsonBuilder()
                .create()
                .fromJson(output.containedJson(), Array<RouteListEntry>::class.java)
                .toList()
        } catch (exception: Throwable) {
            throw CouldNotExtractJsonException(output, exception)
        }

        ReadAction
            .nonBlocking(Callable {
                RouteListEntryEnhancer(
                    PhpIndex.getInstance(project),
                    ModuleManager.getInstance(project),
                ).enhance(routes)
            })
            .inSmartMode(project)
            .finishOnUiThread(ModalityState.NON_MODAL) { introspectedRoutes ->
                publish(introspectedRoutes)
            }
            .submit(NonUrgentExecutor.getInstance())
    }
}

