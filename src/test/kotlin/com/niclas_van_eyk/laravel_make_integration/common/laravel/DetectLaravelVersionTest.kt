package com.niclas_van_eyk.laravel_make_integration.common.laravel

import com.niclas_van_eyk.laravel_make_integration.common.composer.ComposerVersion
import org.junit.jupiter.api.Test

internal class DetectLaravelVersionTest {
    @Test
    fun it_detects_the_version() {
        val lockfileContents = this::class.java.classLoader.getResource("example-composer.lock")?.readText()

        kotlin.test.assertNotNull(lockfileContents)

        val detectedVersion = DetectLaravelVersion.fromLockfileContents(lockfileContents)

        kotlin.test.assertNotNull(detectedVersion)
        kotlin.test.assertEquals(ComposerVersion(8, 13, 0), detectedVersion)
    }
}
