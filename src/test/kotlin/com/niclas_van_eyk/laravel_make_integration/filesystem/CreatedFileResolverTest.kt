package com.niclas_van_eyk.laravel_make_integration.filesystem

import com.niclas_van_eyk.laravel_make_integration.actions.SubCommand
import com.niclas_van_eyk.laravel_make_integration.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.laravel.LaravelProject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CreatedFileResolverTest {
    val project = LaravelProject("/Foo/Bar")
    val command = SubCommand("test", "/app/imaginary/tests")
    val resolver = CreatedFileResolver(project)

    @Test
    fun getCreatedFilePath() {
        assertResolvesTo("app/imaginary/tests/MyNamespace/MyClass.php", ArtisanMakeParameters("MyNamespace/MyClass", mutableListOf("")))
        assertResolvesTo("app/imaginary/tests/MyNamespace/MyClass.php", ArtisanMakeParameters("MyNamespace/MyClass", mutableListOf("test 123")))
    }

    private fun assertResolvesTo(expected: String, input: ArtisanMakeParameters) {
        val suggestion = resolver.getCreatedFilePath(command, input)

        assertEquals("${project.paths.base}/$expected", suggestion)
    }

}