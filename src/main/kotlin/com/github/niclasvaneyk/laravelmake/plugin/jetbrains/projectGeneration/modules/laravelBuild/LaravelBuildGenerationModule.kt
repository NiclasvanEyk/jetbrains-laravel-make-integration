package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelBuild

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import java.awt.GridBagLayout
import kotlin.reflect.KClass

@Suppress("UnstableApiUsage")
class LaravelBuildGenerationModule: LaravelProjectGenerationModule<LaravelBuildSettings>()  {
    companion object { const val KEY = "laravel.build" }
    override val settingsClass: KClass<LaravelBuildSettings> get() = LaravelBuildSettings::class
    override val label: String get() = "laravel.build"
    override val key: String get() = KEY

    override fun ui(): DialogPanel {
        return panel {
            row("Laravel Build") {  }
        }
    }

    override fun strategy(settings: LaravelBuildSettings): LaravelProjectGenerationStrategy<LaravelBuildSettings> {
        return LaravelBuildGenerationStrategy(settings)
    }
}