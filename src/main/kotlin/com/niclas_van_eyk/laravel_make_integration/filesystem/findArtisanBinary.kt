package com.niclas_van_eyk.laravel_make_integration.filesystem

import com.intellij.openapi.vfs.VirtualFile

fun findArtisanBinaryDirectory(start: VirtualFile, stop: String): VirtualFile? {
    var node = start
    var found = false

    while (!node.canonicalPath.equals(stop) || !node.exists()) {
        if (node.findFileByRelativePath("artisan") === null) {
            // Otherwise this could lead to
            if (node.path == "/") break

            node = node.parent
            continue
        }

        found = true
        break
    }

    // Look for the binary one more time in the stop directory
    if (!found && node.findFileByRelativePath("artisan") !== null) {
        found = true
    }

    if (!found ) return null

    return node
}