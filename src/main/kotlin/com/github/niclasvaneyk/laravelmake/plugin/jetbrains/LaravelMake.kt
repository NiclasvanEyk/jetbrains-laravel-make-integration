package com.github.niclasvaneyk.laravelmake.plugin.jetbrains

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.extensions.PluginId

class LaravelMake {
    companion object {
        // See plugin.xml
        val PLUGIN_ID = PluginId.getId("com.niclas-van-eyk.laravel-make-integration")

        fun <T : Any> extensionName(name: String): ExtensionPointName<T> = ExtensionPointName.create(
            "com.niclas-van-eyk.laravel-make-integration.$name"
        )
    }
}