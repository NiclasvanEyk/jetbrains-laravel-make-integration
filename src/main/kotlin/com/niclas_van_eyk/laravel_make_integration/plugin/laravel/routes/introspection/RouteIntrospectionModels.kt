package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection

import com.google.gson.annotations.SerializedName
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

/**
 * As returned by `artisan route:list --json`.
 */
data class RouteListEntry(
    @SerializedName("domain") val domain: String?,
    @SerializedName("method") val method: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("name") val name: String?,
    @SerializedName("action") val rawAction: String,
    @SerializedName("middleware") private val rawMiddleware: Any,
) {
    val middleware get(): List<String> {
        if (rawMiddleware is ArrayList<*>) {
            return rawMiddleware.toList() as List<String>
        }

        if (rawMiddleware is String) {
            return rawMiddleware.split("\n")
        }

        return emptyList()
    }
}

enum class RouteOrigin {
    VENDOR,
    PROJECT,
    UNKNOWN,
}

data class IntrospectedMiddleware(
    val name: String,
    val parameters: List<String>,
) {
    companion object {
        fun fromString(middleware: String): IntrospectedMiddleware {
            val parts = middleware.split(":")
            val name = parts[0]

            if (parts.size <= 1) {
                return IntrospectedMiddleware(name, emptyList())
            }

            val parameters = parts[1].split(",")
            return IntrospectedMiddleware(name, parameters)
        }
    }
}

data class BasicRouteInformation (
    val path: String,
    val httpMethod: String,
    val name: String?,
    val middleware: List<IntrospectedMiddleware>,
)

sealed class IntrospectedRoute (
    basicRouteInformation: BasicRouteInformation,
    val origin: RouteOrigin,
) {
    val path: String = basicRouteInformation.path
    val httpMethod: String = basicRouteInformation.httpMethod
    val name: String? = basicRouteInformation.name
    val middleware: List<IntrospectedMiddleware> = basicRouteInformation.middleware
}

class ClosureRoute(
    basicRouteInformation: BasicRouteInformation,
): IntrospectedRoute(basicRouteInformation, RouteOrigin.UNKNOWN) {
}

class ControllerRoute(
    basicRouteInformation: BasicRouteInformation,
    origin: RouteOrigin,
    val `class`: PhpClass,
    val method: Method,
    val formRequest: PhpClass?,
): IntrospectedRoute(basicRouteInformation, origin) {
    val isInvokableControllerRoute get() = method.name == PhpClass.INVOKE
}
