package com.github.niclasvaneyk.laravelmake.common.filesystem

import java.io.File
import java.nio.file.Paths
import java.util.UUID

/**
 * Creates a new temporary directory with a unique filename.
 */
fun temporaryDirectory(): File {
    return Paths
        .get(System.getProperty("java.io.tmpdir"), "laravel-make-${UUID.randomUUID()}")
        .toFile()
        .apply {
            mkdirs()
        }
}