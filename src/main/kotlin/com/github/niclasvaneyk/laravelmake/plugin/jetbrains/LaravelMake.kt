package com.github.niclasvaneyk.laravelmake.plugin.jetbrains

import com.intellij.openapi.extensions.ExtensionPointName

class LaravelMake {
    companion object {
        fun <T> extensionName(name: String): ExtensionPointName<T> = ExtensionPointName.create(
            "com.niclas-van-eyk.laravel-make-integration.$name"
        )
    }
}