package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.openapi.vfs.VirtualFile

class Project(base: VirtualFile) {
    val artisan: Artisan = Artisan(base)
    val paths: Paths = Paths(base)
}