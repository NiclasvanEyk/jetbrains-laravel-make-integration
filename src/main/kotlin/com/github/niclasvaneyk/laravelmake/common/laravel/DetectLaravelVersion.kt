package com.github.niclasvaneyk.laravelmake.common.laravel

import com.github.niclasvaneyk.laravelmake.common.composer.ComposerVersion
import java.io.File

class DetectLaravelVersion {
    companion object {
        fun fromLockfile(lockFile: File): ComposerVersion? {
            if (!lockFile.exists() || !lockFile.isFile) {
                return null
            }

            return fromLockfileContents(lockFile.readText())
        }

        fun fromLockfileContents(contents: String): ComposerVersion? {
            val laravelVersionRegex =
                """"name": "laravel/framework",\s*"version":\s*"v([0-9]+\.[0-9]+\.[0-9]+)""".toRegex(
                    setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
                )
            val matchResult = laravelVersionRegex.find(contents)?.groups
            val versionString = matchResult?.get(1)?.value

            if (versionString != null) {
                return ComposerVersion(versionString)
            }

            return null
        }
    }
}
