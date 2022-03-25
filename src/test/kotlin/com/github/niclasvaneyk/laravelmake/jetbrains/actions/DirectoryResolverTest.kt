package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem

import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.DirectoryResolver
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DirectoryResolverTest {
    private val resolver = DirectoryResolver("/foo/bar/baz")

    @Test
    fun couldPointToDefaultFolder() {
        assertPointsToDefaultFolder("/foo")
        assertPointsToDefaultFolder("/foo/bar")
        assertPointsToDefaultFolder("/foo/bar/baz")
        assertPointsToDefaultFolder("/foo/bar/baz/")
        assertPointsToDefaultFolder("/foo/bar/baz/below")

        assertDoesNotPointToDefaultFolder("/other/parent")
        assertDoesNotPointToDefaultFolder("/foo/bar/other/parent")
    }

    private fun assertPointsToDefaultFolder(path: String) {
        assertTrue(
            resolver.couldPointToDefaultFolder(path),
            "Failed asserting that '$path' points to '${resolver.defaultFolder}'!"
        )
    }

    private fun assertDoesNotPointToDefaultFolder(path: String) {
        assertFalse(
            resolver.couldPointToDefaultFolder(path),
            "Failed asserting that '$path' does not point to '${resolver.defaultFolder}'!"
        )
    }

    @Test
    fun isAboveDefaultDirectory() {
        assertAboveDefaultFolder("/foo")
        assertAboveDefaultFolder("/foo/bar")

        assertNotAboveDefaultFolder("/foo/bar/baz")
        assertNotAboveDefaultFolder("/foo/bar/baz/")
        assertNotAboveDefaultFolder("/other/root")
        assertNotAboveDefaultFolder("/foo/bar/baz/below")
    }

    private fun assertAboveDefaultFolder(path: String) {
        assertTrue(
            resolver.isAboveDefaultDirectory(path),
            "Failed asserting that '$path' is above '${resolver.defaultFolder}'!"
        )
    }

    private fun assertNotAboveDefaultFolder(path: String) {
        assertFalse(
            resolver.isAboveDefaultDirectory(path),
            "Failed asserting that '$path' is above '${resolver.defaultFolder}'!"
        )
    }

    @Test
    fun isBelowDefaultDirectory() {
        assertBelowDefaultFolder("/foo/bar/baz/baz")
        assertBelowDefaultFolder("/foo/bar/baz")

        // Above
        assertNotBelowDefaultFolder("/foo")
        assertNotBelowDefaultFolder("/foo/bar")

        // Different directories
        assertNotBelowDefaultFolder("/other/root")
    }

    private fun assertBelowDefaultFolder(path: String) {
        assertTrue(
            resolver.isBelowDefaultDirectory(path),
            "Failed asserting that '$path' is below '${resolver.defaultFolder}'!"
        )
    }

    private fun assertNotBelowDefaultFolder(path: String) {
        assertFalse(
            resolver.isBelowDefaultDirectory(path),
            "Failed asserting that '$path' is below '${resolver.defaultFolder}'!"
        )
    }
}
