package com.niclas_van_eyk.laravel_make_integration.laravel

import com.google.common.collect.ImmutableList
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class LaravelProject(path: String) {
    val artisan: Artisan = Artisan(path)
    val paths: LaravelProjectPaths = LaravelProjectPaths(path)

    constructor(base: VirtualFile) : this(base.path)
}

/**
 * Responsible for validating and creating a [LaravelProject].
 */
class LaravelProjectFactory(private val path: String) {
    private val _errors = ArrayList<String>()
    val errors: ImmutableList<String>
        get() = ImmutableList.copyOf(_errors)

    fun build(): LaravelProject? {
        if (!File(path).exists()) {
            addError("'$path' does not exist!")
            return null
        }

        if (!File(path).isDirectory) {
            addError("'$path' is not a directory and therefore can't be a base for a Laravel project!")
            return null
        }

        if (!Artisan(path).exists) {
            addError("No artisan binary found in directory '$path'!")
            return null
        }

        return LaravelProject(path)
    }

    private fun addError(message: String) {
        _errors.add(message)
    }
}
