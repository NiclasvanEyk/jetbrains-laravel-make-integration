package com.niclas_van_eyk.laravel_make_integration.actions

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal abstract class ArtisanMakeActionTest<T: ExecutesArtisanMake> {
    abstract fun createAction(): T

    lateinit var action: T

    @BeforeEach
    internal fun setUp() {
        action = createAction()
    }

    fun make(target: File, name: String, params: List<String>): File {
        return action.make(target, name, params)
    }

    @Test
    fun targetCanBeOpenedAfterExecutingMakeWithoutParameters() {
        throw NotImplementedError()
    }
}
