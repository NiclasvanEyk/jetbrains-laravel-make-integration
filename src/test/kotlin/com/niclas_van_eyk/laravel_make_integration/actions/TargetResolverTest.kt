package com.niclas_van_eyk.laravel_make_integration.actions

import com.niclas_van_eyk.laravel_make_integration.filesystem.DirectoryResolver
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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

        assertNamespaceSuggestedFor("$controllerFolder/Foo/Bar", "Foo/Bar/");
        assertNamespaceSuggestedFor("$controllerFolder/Foo/Bar/", "Foo/Bar/");
    }

    private fun assertNamespaceSuggestedFor(path: String, suggestion: String) {
        assertEquals(suggestion, resolver.suggestInitialInputFor(path, projectBasePath))
    }

    private fun assertNoNamespaceSuggestedFor(path: String) {
        assertEquals("", resolver.suggestInitialInputFor(path, projectBasePath))
    }

    private fun projectPath(path: String): String {
        return "$projectBasePath/$path"
    }
}