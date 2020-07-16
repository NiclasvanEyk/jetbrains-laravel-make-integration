package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class Paths(private val _base: VirtualFile) {
    /**
     * Returns the path from the project root or null, if the given path is not inside the project.
     */
    fun fromProjectRoot(absoluteFilePath: String): String {
        return absoluteFilePath.replace(base, "")
    }

    fun path(relativePath: String): String {
        return "$base/$relativePath"
    }

    fun hasFolder(relativePathToFolder: String): Boolean {
        var target = File(path(relativePathToFolder))

        return target.exists() && target.isDirectory;
    }

    val base: String
        get() = _base.canonicalPath ?: ""
}