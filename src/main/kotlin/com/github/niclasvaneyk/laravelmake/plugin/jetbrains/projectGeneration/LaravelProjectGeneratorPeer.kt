package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.composer.ComposerSettings
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelBuild.LaravelBuildGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelBuild.LaravelBuildSettings
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationModule
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class LaravelProjectGeneratorPeer(
    val modules: List<LaravelProjectGenerationModule<*>>,
): ProjectGeneratorPeer<LaravelProjectGeneratorSettings<*>> {
    private var selectedModule = LaravelBuildGenerationModule.KEY
    private val moduleUisByKey = mutableMapOf<String, DialogPanel>()
    private val container = JPanel()

    override fun getComponent(): JComponent {
        container.add(panel {
            buttonGroup({ selectedModule }, { newSelectedModule ->
                selectedModule = newSelectedModule
                onModuleChanged(newSelectedModule)
            }) {
                row {
                    for (module in modules) {
                        radioButton(module.label, module.key)
                    }
                }
            }
        })

        for (module in modules) {
            val modulePanel = module.ui()
            moduleUisByKey[module.key] = modulePanel
            container.add(modulePanel)
        }

        return container
    }

    override fun buildUI(step: SettingsStep) {
        step.addSettingsComponent(container)
    }

    private fun onModuleChanged(newModule: String) {
        moduleUisByKey.values.forEach { it.isVisible = false }
        moduleUisByKey[newModule]!!.isVisible = true
    }

    override fun getSettings(): LaravelProjectGeneratorSettings<*> {
        return LaravelProjectGeneratorSettings(
            "~/IdeaProjects",
            ComposerSettings(
                executablePath = "/usr/local/bin/composer",
                projectName = "example",
            )
        )
    }

    override fun validate(): ValidationInfo? {
        return null
    }

    override fun isBackgroundJobRunning(): Boolean {
        return false
    }
}