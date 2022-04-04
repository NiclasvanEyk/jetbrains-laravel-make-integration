package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.composer.ComposerGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelBuild.LaravelBuildGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelInstaller.LaravelInstallerGenerationModule
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.platform.LaravelProjectGenerationModule
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.WebProjectTemplate
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.platform.ProjectTemplate
import com.intellij.platform.ProjectTemplatesFactory
import com.jetbrains.php.config.generation.PhpEmptyTemplatesFactory
import icons.PhpIcons
import javax.swing.Icon

class LaravelProjectGeneratorSettings<S: Any>(
    val path: String,
    val strategySettings: S,
)

class LaravelProjectGeneratorFactory: ProjectTemplatesFactory() {
    override fun getGroups(): Array<String> = arrayOf(
        PhpEmptyTemplatesFactory.PHP_PROJECT_TEMPLATE_GROUP
    )

    override fun getGroupIcon(group: String?): Icon = PhpIcons.PhpIcon

    override fun createTemplates(group: String?, context: WizardContext): Array<ProjectTemplate> {
        return arrayOf(LaravelProjectGenerator())
    }
}

/**
 * This generator delegates the creation to the more specialized
 * [LaravelProjectGenerationModule] located in the `strategies` package.
 *
 * This way the user has a single "Laravel" entry for generation, and can then
 * choose how to generate the project, e.g. using https://laravel.build, the
 * Laravel installer or other 3rd-party options.
 */
class LaravelProjectGenerator: WebProjectTemplate<LaravelProjectGeneratorSettings<*>>() {
    override fun getName() = "Laravel"
    override fun getIcon() = LaravelIcons.LaravelLogo
    override fun getDescription() = "Generate a new Laravel application"
    override fun createPeer() = LaravelProjectGeneratorPeer(generationModules)

    private val generationModules = listOf<LaravelProjectGenerationModule<*>>(
        LaravelBuildGenerationModule(),
        LaravelInstallerGenerationModule(),
        ComposerGenerationModule(),
    )

    override fun generateProject(
        project: Project,
        baseDir: VirtualFile,
        settings: LaravelProjectGeneratorSettings<*>,
        module: Module
    ) = generationModules
        .firstNotNullOf { it.buildStrategyIfApplicable(settings.strategySettings) }
        .generateProject(project, baseDir, settings.path, module)
}