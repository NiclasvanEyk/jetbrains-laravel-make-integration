package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import com.intellij.bigdatatools.visualization.charts.utils.getAsStringOrNull
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import java.lang.reflect.Type

sealed class RouteAction

class ControllerRouteAction(
    @SerializedName("controller") val controller: String,
    @SerializedName("method") val method: String,
): RouteAction()

class ClosureRouteAction(
    @SerializedName("file") val file: String,
    @SerializedName("start") val start: Int,
    @SerializedName("end") val end: Int,
): RouteAction()

class RouteActionAdapter: JsonDeserializer<RouteAction> {
    override fun deserialize(
        element: JsonElement?,
        type: Type?,
        context: JsonDeserializationContext?
    ): RouteAction {
        if (element === null) throw JsonParseException("empty json element")
        val json = element.asJsonObject
        val actionType = json.getAsStringOrNull("type") ?: throw JsonParseException("missing type specifier")

        return when (actionType) {
            "method" -> ControllerRouteAction(
                controller = json.get("controller").asString,
                method = json.get("method").asString,
            )
            "closure" -> ClosureRouteAction(
                file = json.get("file").asString,
                start = json.get("start").asInt,
                end = json.get("end").asInt,
            )

            else -> throw JsonParseException("unknown type '$type'")
        }
    }
}

/**
 * As returned by `artisan route:list --json`.
 */
data class RouteListEntry(
    @SerializedName("verbs") val verbs: List<String>,
    @SerializedName("path") val path: String,
    @SerializedName("name") val name: String?,
    @SerializedName("action") val action: RouteAction,
    @SerializedName("middleware") val middleware: List<String>,
)

enum class RouteOrigin {
    VENDOR,
    PROJECT,
    UNKNOWN,
}

data class IntrospectedMiddleware(
    val name: String,
    val parameters: List<String>,
) {
    val basename = name.substringAfterLast("\\")

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
    val normalizedHttpMethod = httpMethod.replace("|head", "", ignoreCase = true)
    val name: String? = basicRouteInformation.name
    val middleware: List<IntrospectedMiddleware> = basicRouteInformation.middleware
}

class ClosureRoute(
    basicRouteInformation: BasicRouteInformation,
    val action: ClosureRouteAction,
): IntrospectedRoute(basicRouteInformation, RouteOrigin.UNKNOWN)

class ControllerRoute(
    basicRouteInformation: BasicRouteInformation,
    origin: RouteOrigin,
    val `class`: PhpClass,
    val method: Method,
    val formRequest: PhpClass?,
): IntrospectedRoute(basicRouteInformation, origin) {
    val isInvokableControllerRoute get() = method.name == PhpClass.INVOKE

    /**
     * This is more reliable than the method object, since the referenced
     * method can be invalidated when editing the source file. By caching
     * the fqn here, we can always retrieve a fresh method instance from
     * the index.
     */
    val fqn = method.fqn
}
