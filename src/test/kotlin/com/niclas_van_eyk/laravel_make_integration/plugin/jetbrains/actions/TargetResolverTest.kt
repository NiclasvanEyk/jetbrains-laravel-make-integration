package com.niclas_van_eyk.laravel_make_integration.plugin.jetbrains.actions

import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.DirectoryResolver
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.jetbrains.TargetResolver
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TargetResolverTest {
    private val projectBasePath = "/Users/Somebody/Projects/Laravel/MyCoolProject"
    private val appFolder = "app"
    private val httpFolder = "$appFolder/Http"
    private val controllerFolder = "$httpFolder/Controllers"
    private val resolver = TargetResolver(DirectoryResolver(controllerFolder))

    @Test
    fun suggestInitialInputFor() {
        assertNoNamespaceSuggestedFor("/Folder/That/Is/Outside/The/Project/For/Some/Reason")
        assertNoNamespaceSuggestedFor(projectPath(projectBasePath))
        assertNoNamespaceSuggestedFor(projectPath(appFolder))
        assertNoNamespaceSuggestedFor(projectPath(httpFolder))
        assertNoNamespaceSuggestedFor(projectPath(controllerFolder))
        assertNoNamespaceSuggestedFor(projectPath("/$controllerFolder"))
        assertNoNamespaceSuggestedFor(projectPath("/$controllerFolder"))
        assertNoNamespaceSuggestedFor(projectPath("/$controllerFolder/"))

        assertNamespaceSuggestedFor("$controllerFolder/Foo/Bar", "Foo/Bar/")
        assertNamespaceSuggestedFor("$controllerFolder/Foo/Bar/", "Foo/Bar/")
    }

    private fun assertNamespaceSuggestedFor(path: String, suggestion: String) {
        assertEquals(suggestion, resolver.suggestInitialInputFor(path, projectBasePath).name)
    }

    private fun assertNoNamespaceSuggestedFor(path: String) {
        assertEquals("", resolver.suggestInitialInputFor(path, projectBasePath).name)
    }

    private fun projectPath(path: String): String {
        return "$projectBasePath/$path"
    }
}
