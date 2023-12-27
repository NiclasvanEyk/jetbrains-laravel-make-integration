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
            path = if (route.path.startsWith("/")) route.path else "/${route.path}",
            httpMethod = route.verbs.joinToString("|"),
            name = route.name,
            middleware = route.middleware.map { IntrospectedMiddleware.fromString(it) }
        )

        when (val action = route.action) {
            is ClosureRouteAction -> return ClosureRoute(basicRouteInformation, action)
            is ControllerRouteAction -> {
                val controller = index.getClassesByFQN(action.controller).firstOrNull() ?: return null
                val method = controller.findMethodByName(action.method) ?: return null

                return ControllerRoute(
                    basicRouteInformation,
                    origin = findRouteOrigin(controller),
                    controller,
                    method,
                    formRequest = findFormRequestFor(method)
                )
            }
        }
    }

    private fun findRouteOrigin(controller: PhpClass): RouteOrigin {
        if (controller.fqn.startsWith("\\App\\")) {
            return RouteOrigin.PROJECT
        }

        val classFile = controller.containingFile.virtualFile

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