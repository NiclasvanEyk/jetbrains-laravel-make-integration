package com.github.niclasvaneyk.laravelmake.common.filesystem

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

/**
 * Moves all files _in_ the [source] to the [destination] and overrides any
 * conflicting files.
 */
fun moveDirectoryContents(source: Path, destination: Path) {
    for (file in Files.newDirectoryStream(source)) {
        Files.move(
            file,
            destination.resolve(file.toFile().name),
            StandardCopyOption.REPLACE_EXISTING,
        )
    }
}