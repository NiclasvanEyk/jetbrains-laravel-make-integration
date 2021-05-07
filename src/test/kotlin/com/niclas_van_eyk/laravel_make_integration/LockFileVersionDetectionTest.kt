package com.niclas_van_eyk.laravel_make_integration

import com.niclas_van_eyk.laravel_make_integration.laravel.ComposerVersion
import com.niclas_van_eyk.laravel_make_integration.laravel.detectLaravelVersionFromLockfileContents
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.Test

class LockFileVersionDetectionTest {
    @Test
    fun it_detects_the_version() {
        val lockfileContents = this::class.java.classLoader.getResource("example-composer.lock")!!.readText()

        assertNotNull(lockfileContents)

        val detectedVersion = detectLaravelVersionFromLockfileContents(lockfileContents)

        assertNotNull(detectedVersion)
        assertEquals(ComposerVersion(8, 13, 0), detectedVersion)
    }
}