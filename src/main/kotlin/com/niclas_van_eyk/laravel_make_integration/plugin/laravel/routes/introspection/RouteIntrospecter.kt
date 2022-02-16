package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection

import com.google.gson.GsonBuilder
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runInEdt
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.startUnderBackgroundProgressAsync
import com.intellij.openapi.rd.util.withBackgroundProgressContext
import com.intellij.util.ui.EDT
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.rd.util.lifetime.Lifetime
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.common.laravel.Artisan
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandRunInfo
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandBasedIntrospecter

class RouteIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    private val project: Project,
) : CommandBasedIntrospecter<List<IntrospectedRoute>>(artisan, progressBar) {
    override val description = "Scanning Laravel routes"
    override val command = CommandRunInfo("route", "list", listOf("--json"))

    override fun onCommandOutput(output: String, publish: (result: List<IntrospectedRoute>) -> Unit) {
        // Sometimes the json contains newlines, which mess with the
        // serialization, that e.g. the action of a route is suddenly null, even
        // though if we remove the newlines everything works fine.
        // This is kinda hacky, but works for routes.
        val sanitizedOutput = output.replace("\n", "")

        val routes = GsonBuilder()
            .create()
            .fromJson(sanitizedOutput, Array<RouteListEntry>::class.java)
            .toList()

        Lifetime.Eternal.startUnderBackgroundProgressAsync("Processing routes", action = {
            runReadAction {
                val enhancer = RouteListEntryEnhancer(PhpIndex.getInstance(project))
                val introspectedRoutes = enhancer.enhance(routes)

                runInEdt {
                    publish(introspectedRoutes)
                }
            }
        })
    }
}

/**
 * Enhances the data returned from `artisan route:list` with some PhpStorm goodies.
 */
private class RouteListEntryEnhancer(private val index: PhpIndex) {
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
        val `class` = index.getClassesByFQN(fqn).first() ?: return null
        val method = `class`.findMethodByName(methodName) ?: return null
        val hasAppNamespace = `class`.fqn.startsWith("\\App\\")

        return ControllerRoute(
            basicRouteInformation,
            origin = if (hasAppNamespace) RouteOrigin.PROJECT else RouteOrigin.VENDOR,
            `class`,
            method,
            formRequest = findFormRequestFor(method)
        )
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
