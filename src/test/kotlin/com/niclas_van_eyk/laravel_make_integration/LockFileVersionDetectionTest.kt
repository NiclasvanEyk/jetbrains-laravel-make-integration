package com.niclas_van_eyk.laravel_make_integration

import com.niclas_van_eyk.laravel_make_integration.laravel.ComposerVersion
import com.niclas_van_eyk.laravel_make_integration.laravel.detectLaravelVersionFromLockfileContents
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LockFileVersionDetectionTest {
    @Test
    fun it_detects_the_version() {
        val lockfileContents = this::class.java.classLoader.getResource("example-composer.lock").readText()
        val detectedVersion = detectLaravelVersionFromLockfileContents(lockfileContents)

        assertNotNull(detectedVersion)
        assertEquals(ComposerVersion(8, 13, 0), detectedVersion)
    }
}