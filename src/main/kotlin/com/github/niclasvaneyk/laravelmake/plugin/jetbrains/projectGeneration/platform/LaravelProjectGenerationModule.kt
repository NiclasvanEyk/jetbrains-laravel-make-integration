package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform

import com.intellij.openapi.ui.DialogPanel
import kotlin.reflect.KClass
import kotlin.reflect.cast

@Suppress("UnstableApiUsage")
abstract class LaravelProjectGenerationModule<S: Any> {
    abstract val settingsClass: KClass<S>
    abstract val label: String
    abstract val key: String

    abstract fun strategy(settings: S): LaravelProjectGenerationStrategy<S>
    abstract fun ui(): DialogPanel

    fun buildStrategyIfApplicable(settings: Any): LaravelProjectGenerationStrategy<*>? {
        if (!this.settingsClass.isInstance(settings)) {
            return null
        }

        return strategy(this.settingsClass.cast(settings))
    }
}