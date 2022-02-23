package com.github.niclasvaneyk.laravelmake.common.composer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ComposerVersionTest {
    @Test
    fun `it can compare different versions`() {
        assertEquals(
            ComposerVersion(8, 0, 0),
            ComposerVersion(8, 0, 0)
        )
        assertTrue(
            ComposerVersion(
                8, 0, 0
            ) >= ComposerVersion(8, 0, 0)
        )
        assertTrue(
            ComposerVersion(
                8, 0, 0
            ) <= ComposerVersion(8, 0, 0)
        )

        assertTrue(
            ComposerVersion(
                9, 0, 0
            ) > ComposerVersion(8, 0, 0)
        )
        assertTrue(
            ComposerVersion(
                8, 1, 0
            ) > ComposerVersion(8, 0, 0)
        )
        assertTrue(
            ComposerVersion(
                8, 0, 1
            ) > ComposerVersion(8, 0, 0)
        )
    }
}
