package com.github.niclasvaneyk.laravelmake.common.laravel

import com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion
import org.junit.jupiter.api.Test

internal class DetectLaravelVersionTest {
    @Test
    fun it_detects_the_version() {
        val lockfileContents = this::class.java.classLoader.getResource("example-composer.lock")?.readText()

        kotlin.test.assertNotNull(lockfileContents)

        val detectedVersion = DetectLaravelVersion.fromLockfileContents(lockfileContents)

        kotlin.test.assertNotNull(detectedVersion)
        kotlin.test.assertEquals(com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion(8, 13, 0), detectedVersion)
    }
}
