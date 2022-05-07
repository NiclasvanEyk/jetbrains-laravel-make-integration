package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.composer

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import kotlin.reflect.KClass

data class ComposerSettings(
    val executablePath: String,
    val projectName: String,
)

class ComposerGenerationModule: LaravelProjectGenerationModule<ComposerSettings>() {
    override val settingsClass: KClass<ComposerSettings> get() = ComposerSettings::class
    override val label: String get() = "Composer"
    override val key: String get() = "composer-create-project"
    override fun strategy(settings: ComposerSettings) = ComposerGenerationStrategy(settings)

    override fun ui(): DialogPanel {
        return panel {
            row("Composer") {
                checkBox("Something")
            }
        }
    }
}