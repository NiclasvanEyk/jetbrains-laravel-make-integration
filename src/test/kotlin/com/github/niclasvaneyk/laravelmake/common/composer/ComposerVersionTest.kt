package com.github.niclasvaneyk.laravelmake.common.composer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ComposerVersionTest {
    @Test
    fun `it can compare different versions`() {
        assertEquals(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0),
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )
        assertTrue(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(
                8,
                0,
                0
            ) >= com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )
        assertTrue(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(
                8,
                0,
                0
            ) <= com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )

        assertTrue(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(
                9,
                0,
                0
            ) > com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )
        assertTrue(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(
                8,
                1,
                0
            ) > com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )
        assertTrue(
            com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(
                8,
                0,
                1
            ) > com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 0, 0)
        )
    }
}
