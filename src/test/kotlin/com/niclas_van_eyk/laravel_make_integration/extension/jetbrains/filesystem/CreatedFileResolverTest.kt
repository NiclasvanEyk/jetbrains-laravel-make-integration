package com.niclas_van_eyk.laravel_make_integration.extension.jetbrains.filesystem

import com.niclas_van_eyk.laravel_make_integration.common.laravel.ArtisanMakeParameters
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.CreatedFileResolver
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.SubCommand
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CreatedFileResolverTest {
    val command = SubCommand("test", "/app/imaginary/tests")
    val resolver = CreatedFileResolver("/Foo/Bar")

    @Test
    fun getCreatedFilePath() {
        assertResolvesTo(
            "app/imaginary/tests/MyNamespace/MyClass.php",
            ArtisanMakeParameters("MyNamespace/MyClass", mutableListOf(""))
        )
        assertResolvesTo(
            "app/imaginary/tests/MyNamespace/MyClass.php",
            ArtisanMakeParameters("MyNamespace/MyClass", mutableListOf("test 123"))
        )
    }

    private fun assertResolvesTo(expected: String, input: ArtisanMakeParameters) {
        val suggestion = resolver.getCreatedFilePath(command, input)

        assertEquals("/Foo/Bar/$expected", suggestion)
    }
}
