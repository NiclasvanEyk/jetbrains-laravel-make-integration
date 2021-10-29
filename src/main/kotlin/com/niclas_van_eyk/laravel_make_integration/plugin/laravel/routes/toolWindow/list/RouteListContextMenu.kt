package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.toolWindow.list

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBPopupMenu
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.ClassBasedRouteAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.ControllerMethodAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.InvocableControllerAction
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.routes.introspection.RouteListEntry
import javax.swing.JMenuItem

class RouteListContextMenu(
    private val route: RouteListEntry,
    project: Project,
) : JBPopupMenu() {
    init {
        add(
            JMenuItem("Jump To Source", AllIcons.Actions.EditSource).apply {
                // Needs to be an invocable controller or a controller method
                isEnabled = route.canJumpToControllerActionSource()
                addActionListener {
                    route.jumpToControllerActionSource(project)
                }
            }
        )

        val formRequestClass = route.formRequestClass(project)

        if (formRequestClass != null) {
            add(
                JMenuItem(
                    "Jump To Request",
                    AllIcons.Actions.EditSource,
                ).apply {
                    addActionListener {
                        formRequestClass.navigate(true)
                    }
                }
            )
        }
    }
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

fun RouteListEntry.jumpToControllerActionSource(project: Project) {
    val clazz = clazz(project) ?: return
    val action = controllerAction
    val methodName = if (action is ControllerMethodAction) action.methodName
    else InvocableControllerAction.INVOKE_METHOD_NAME
    (clazz.findMethodByName(methodName) ?: clazz).navigate(true)
}
