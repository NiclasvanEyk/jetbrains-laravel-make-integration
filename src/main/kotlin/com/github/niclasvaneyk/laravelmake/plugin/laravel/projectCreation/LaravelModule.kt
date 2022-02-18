package com.github.niclasvaneyk.laravelmake.plugin.laravel.projectCreation

import com.intellij.framework.FrameworkType
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProviderBase
import com.intellij.ide.util.frameworkSupport.FrameworkVersion
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.libraries.Library
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import javax.swing.JComponent
import javax.swing.JLabel


class LaravelModuleType: ModuleType<LaravelModuleBuilder>(ID) {
    companion object {
        const val ID = "LARAVEL"
        fun getInstance() = ModuleTypeManager.getInstance().findByID(ID) as LaravelModuleType
    }

    override fun getName() = "Laravel"
    override fun getDescription() = "Create a new Laravel Project"
    override fun getNodeIcon(isOpened: Boolean) = LaravelIcons.LaravelLogo

    override fun createModuleBuilder() = LaravelModuleBuilder()
}

class LaravelModuleBuilder: ModuleBuilder() {
    override fun getModuleType() = LaravelModuleType.getInstance()
}

class LaravelModuleStep: ModuleWizardStep() {
    override fun getComponent(): JComponent {
        return JLabel("Do Laravel Stuff!")
    }

    override fun updateDataModel() {
        // Yes yes
    }
}

class LaravelDotBuildFramework: FrameworkType(LaravelDotBuildFramework::class.java.name) {
    override fun getPresentableName() = "laravel.build"
    override fun getIcon() = LaravelIcons.LaravelLogo
}

class LaravelDotBuildFrameworkProvider: FrameworkSupportProviderBase(
    LaravelDotBuildFramework::class.java.name,
    "laravel.build"
) {
    override fun isEnabledForModuleType(moduleType: ModuleType<*>)
        = moduleType is LaravelModuleType

    override fun addSupport(
        module: Module,
        rootModel: ModifiableRootModel,
        version: FrameworkVersion?,
        library: Library?
    ) {
        // TODO
    }
}
