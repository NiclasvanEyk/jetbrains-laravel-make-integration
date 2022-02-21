package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.google.gson.GsonBuilder
import com.intellij.openapi.application.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.NonUrgentExecutor
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.IntrospectionList
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandRunInfo
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CouldNotExtractJsonException
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.vfs.VirtualFileManager
import java.util.concurrent.Callable

class RouteIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    private val project: Project,
) : CommandBasedIntrospecter<List<IntrospectedRoute>>(artisan, progressBar) {
    override val description = "Scanning Laravel routes"
    override val command = CommandRunInfo("route", "list", listOf("--json"))

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(
            RoutesFileChangeListener(this),
            project
        )
    }

    companion object {
        protected val log = Logger.getInstance(IntrospectionList::class.java)
    }

    override fun onCommandOutput(output: String, publish: (result: List<IntrospectedRoute>) -> Unit) {
        // Sometimes the json contains newlines, which mess with the
        // serialization, that e.g. the action of a route is suddenly null, even
        // though if we remove the newlines everything works fine.
        // This is kinda hacky, but works for routes.
        val sanitizedOutput = output.replace("\n", "")

        val routes: List<RouteListEntry>
        try {
            routes = GsonBuilder()
                .create()
                .fromJson(sanitizedOutput, Array<RouteListEntry>::class.java)
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

/**
 * Enhances the data returned from `artisan route:list` with some PhpStorm goodies.
 */
private class RouteListEntryEnhancer(
    private val index: PhpIndex,
    private val moduleManager: ModuleManager,
) {
    fun enhance(routes: List<RouteListEntry>) = routes.mapNotNull { route -> enhance(route) }

    private fun enhance(route: RouteListEntry): IntrospectedRoute? {
        val basicRouteInformation = BasicRouteInformation(
            // I personally think it's nicer if *all* routes start with a /
            path = if (route.uri.startsWith("/")) route.uri else "/${route.uri}",
            httpMethod = route.method,
            name = route.name,
            middleware = route.middleware.map { IntrospectedMiddleware.fromString(it) }
        )

        if (route.rawAction.equals("closure", ignoreCase = true)) {
            return ClosureRoute(basicRouteInformation)
        }

        val actionParts = route.rawAction.split("@")
        val fqn = actionParts[0]
        val methodName = actionParts.getOrNull(1) ?: PhpClass.INVOKE
        val `class` = index.getClassesByFQN(fqn).firstOrNull() ?: return null
        val method = `class`.findMethodByName(methodName) ?: return null


        return ControllerRoute(
            basicRouteInformation,
            origin = findRouteOrigin(`class`),
            `class`,
            method,
            formRequest = findFormRequestFor(method)
        )
    }

    private fun findRouteOrigin(`class`: PhpClass): RouteOrigin {
        if (`class`.fqn.startsWith("\\App\\")) {
            return RouteOrigin.PROJECT
        }

        val classFile = `class`.containingFile.virtualFile

        for (module in moduleManager.modules) {
            if (module.moduleContentScope.contains(classFile)) {
                return RouteOrigin.PROJECT
            }
        }

        // If it is not contained in any of the modules content, we can safely
        // assume it is a vendor route. Closure-based routes are handled before
        // this is called, so no need to use RouteOrigin.UNKNOWN here.
        return RouteOrigin.VENDOR
    }

    private fun findFormRequestFor(method: Method): PhpClass? {
        method.parameters.forEach { parameter ->
            parameter.declaredType.types
                // quick way to filter out built-in types
                .filter { it.contains("\\") }
                .mapNotNull { type -> index.getClassesByFQN(type).firstOrNull() }
                .forEach { `class` ->
                    val containsFormRequest = `class`.extendsList.referenceElements.any {
                        it.declaredType.toString().contains("Illuminate\\Foundation\\Http\\FormRequest")
                    }

                    if (containsFormRequest) {
                        return `class`
                    }
                }
        }

        return null
    }
}
