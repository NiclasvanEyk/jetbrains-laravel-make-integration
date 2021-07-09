package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.list

import com.intellij.openapi.project.Project
import com.intellij.ui.ListSpeedSearch
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.IntrospectionList
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.TriggersRender
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.IntrospectionSubject
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.LoadedState
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.RevalidatingState
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.ClassBasedRouteAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.ControllerMethodAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.InvocableControllerAction
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.introspection.RouteListEntry
import java.time.LocalDateTime
import javax.swing.*
import kotlin.reflect.KProperty

class RouteList(
    routeUpdates: IntrospectionSubject<List<RouteListEntry>>,
    private val project: Project,
    onRouteSelected: (RouteListEntry?) -> Unit,
) : IntrospectionList<RouteListEntry>(routeUpdates) {
    var showMiddlewareParameters by TriggersRender(false, this)
    var showApplicationRoutes by TriggersRender(true, this)
    var showVendorRoutes by TriggersRender(true, this)

    init {
        addMouseListener(RouteListMouseListener(this, project, onRouteSelected))

        selectionMode = ListSelectionModel.SINGLE_SELECTION
        cellRenderer = RouteListCellRenderer(showMiddlewareParameters, project)

        ListSpeedSearch(this) { it.uri }

        subscribeToModelUpdates()
    }

    override fun deriveVisibleModel(newModel: List<RouteListEntry>): List<RouteListEntry> {
        return newModel
            .filter { showApplicationRoutes || !it.isApplicationRoute(project) }
            .filter { showVendorRoutes || !it.isVendorRoute(project) }
    }
}

fun RouteListEntry.clazz(project: Project): PhpClass? {
    val action = controllerAction

    if (action !is ClassBasedRouteAction) return null

    return PhpIndex.getInstance(project).getClassesByFQN(action.className).first()
}

fun RouteListEntry.jumpToControllerActionSource(project: Project) {
    val clazz = clazz(project) ?: return
    val action = controllerAction
    val methodName = if (action is ControllerMethodAction) action.methodName
    else InvocableControllerAction.INVOKE_METHOD_NAME
    (clazz.findMethodByName(methodName) ?: clazz).navigate(true)
}

fun RouteListEntry.isVendorRoute(project: Project): Boolean {
    return !isApplicationRoute(project)
}

fun RouteListEntry.isApplicationRoute(project: Project): Boolean {
    val clazz = clazz(project) ?: return false

    // very rudimentary, can surely be improved using clazz.containingFile
    return clazz.fqn.startsWith("\\App\\")
}

fun RouteListEntry.formRequestClass(project: Project): PhpClass? {
    val action = controllerAction

    if (action !is ClassBasedRouteAction) return null

    val clazz = PhpIndex.getInstance(project).getClassesByFQN(action.className).first() ?: return null
    val methodName = if (action is ControllerMethodAction) action.methodName
    else InvocableControllerAction.INVOKE_METHOD_NAME

    clazz.findMethodByName(methodName)?.parameters?.forEach { parameter ->
        parameter.declaredType.types
            // quick way to filter out built-in types
            .filter { it.contains("\\") }
            .mapNotNull { type -> PhpIndex.getInstance(project).getClassesByFQN(type).firstOrNull() }
            .forEach { clazz ->
                val containsFormRequest = clazz.extendsList.referenceElements.any {
                    it.declaredType.toString().contains("Illuminate\\Foundation\\Http\\FormRequest")
                }

                if (containsFormRequest) {
                    return clazz
                }
            }
    }

    return null
}

fun RouteListEntry.canJumpToControllerActionSource() = controllerAction is ClassBasedRouteAction
