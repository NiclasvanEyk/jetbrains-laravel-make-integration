package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.seeders

import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.extends
import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.isPhpIdentifier
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass

class RunSeederLineMarkerProvider: LineMarkerProviderDescriptor() {
    override fun getName() = "Run seeder"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        element.project.service<LaravelMakeProjectService>().application ?: return null

        if (!element.isPhpIdentifier()) return null

        val parent = element.parent
        if (parent !is PhpClass) return null
        if (!parent.extends("\\Illuminate\\Database\\Seeder")) return null

//        return RunLineMarkerInfo
//        return RunLineMarkerContributor.withExecutorActions()
        return RunSeederLineMarker(element, parent.fqn)
    }
}