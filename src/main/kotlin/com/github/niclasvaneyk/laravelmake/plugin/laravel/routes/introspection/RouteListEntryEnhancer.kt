package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.intellij.openapi.module.ModuleManager
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

/**
 * Enhances the data returned from `artisan route:list` with some PhpStorm goodies.
 */
class RouteListEntryEnhancer(
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