package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelInstaller

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationStrategy
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import kotlin.reflect.KClass

@Suppress("UnstableApiUsage")
class LaravelInstallerGenerationModule
    : LaravelProjectGenerationModule<LaravelInstallerSettings>() {
    override val settingsClass: KClass<LaravelInstallerSettings>
    get() = LaravelInstallerSettings::class
    override val label: String get() = "Laravel Installer"
    override val key: String get() = "installer"

    override fun ui(): DialogPanel {
        return panel {
            row("Laravel Installer") {
                checkBox("Something")
            }
        }
    }

    override fun strategy(settings: LaravelInstallerSettings)
        = LaravelInstallerGenerationStrategy(settings)
}

