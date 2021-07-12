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
import javax.swing.event.ListSelectionListener
import kotlin.reflect.KProperty

class RouteList(
    routeUpdates: IntrospectionSubject<List<RouteListEntry>>,
    private val project: Project,
    onRouteSelected: (RouteListEntry?) -> Unit,
) : IntrospectionList<RouteListEntry>(routeUpdates) {
    var showMiddlewareParameters by TriggersRender(false, this)
    var showApplicationRoutes by TriggersRender(true, this)
    var showVendorRoutes by TriggersRender(true, this)

    var previouslySelectedRoute: RouteListEntry? = null

    init {
        addMouseListener(RouteListMouseListener(this, project, onRouteSelected))
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        selectionModel.addListSelectionListener {
            if (this.selectedValue !== previouslySelectedRoute) {
                onRouteSelected(this.selectedValue)
                previouslySelectedRoute = this.selectedValue
            }
        }

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

fun RouteListEntry.isVendorRoute(project: Project): Boolean {
    // We could also parse the psr4 namespace declarations of the
    // devDependencies to maybe mark some routes "green"/test-color
    // to highlight routes contributed by debug tools like horizon,
    // telescope, ignition, etc.
    // We could then also link the package responsible for the route in the
    // documentation panel.
    return !isApplicationRoute(project)
}

fun RouteListEntry.isApplicationRoute(project: Project): Boolean {
    val clazz = clazz(project) ?: return false

    // very rudimentary, can surely be improved using clazz.containingFile
    return clazz.fqn.startsWith("\\App\\")
}
