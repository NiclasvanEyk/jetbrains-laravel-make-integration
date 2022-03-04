package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.microservices

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.intellij.microservices.endpoints.*
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ModificationTracker

class LaravelEndpointGroup(val endpoints: List<IntrospectedRoute>)
typealias LaravelEndpoint = IntrospectedRoute

/**
 * Right now this just lists the endpoints in the Endpoints view, which is not
 * much more useful than what the Laravel toolwindow provides.
 *
 * Maybe makes more sense to look more into this in the future.
 *
 * As of now this implementation is VERY much work in progress!
 */
@Suppress("UnstableApiUsage")
class LaravelEndpointsProvider: EndpointsProvider<LaravelEndpointGroup, LaravelEndpoint> {
    override val endpointType get() = API_DEFINITION_TYPE
    override val presentation get() = FrameworkPresentation(
        "Laravel",
        "Laravel",
        LaravelIcons.LaravelLogo,
    )

    override fun getEndpointData(group: LaravelEndpointGroup, endpoint: LaravelEndpoint, dataId: String): Any? {
        return null
    }

    override fun getEndpointGroups(project: Project, filter: EndpointsFilter): Iterable<LaravelEndpointGroup> {
        val application = project.service<LaravelMakeProjectService>().application ?: return emptyList()
        val routes = application.introspection.routeIntrospecter.snapshot ?: emptyList()

        return listOf(LaravelEndpointGroup(routes))
    }

    override fun getEndpoints(group: LaravelEndpointGroup) = group.endpoints

    override fun getEndpointPresentation(group: LaravelEndpointGroup, endpoint: LaravelEndpoint): ItemPresentation {
        return object: ItemPresentation {
            override fun getPresentableText() = endpoint.path
            override fun getIcon(unused: Boolean) = LaravelIcons.LaravelLogo
        }
    }

    override fun getModificationTracker(project: Project): ModificationTracker {
        // This is definitely not ready!
        return ModificationTracker {
            val application = project.service<LaravelMakeProjectService>().application ?: return@ModificationTracker -1
            val routes = application.introspection.routeIntrospecter.snapshot ?: emptyList()

            return@ModificationTracker routes.size.toLong()
        }
    }

    override fun getStatus(project: Project): EndpointsProvider.Status {
        val application = project.service<LaravelMakeProjectService>().application ?: return EndpointsProvider.Status.UNAVAILABLE
        val hasRoutes = application.introspection.routeIntrospecter.snapshot?.isNotEmpty() == true

        return if (hasRoutes) EndpointsProvider.Status.HAS_ENDPOINTS
            else EndpointsProvider.Status.AVAILABLE
    }

    override fun isValidEndpoint(group: LaravelEndpointGroup, endpoint: LaravelEndpoint): Boolean {
        return true
    }
}