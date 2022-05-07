package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.github.niclasvaneyk.laravelmake.common.jetbrains.codeInsight.daemon.SimpleTooltipLineMarkerInfo
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelIcons
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import icons.DatabaseIcons
import java.io.File

class MigrationStatusLineMarkerProvider: LineMarkerProviderDescriptor() {
    override fun getName() = "Laravel migrations, that were executed"
    override fun getIcon() = DatabaseIcons.ToolwindowDatabaseChanges

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val migration = element.migration ?: return null

        // This case is handled by another contributor
        if (!migration.ran) {
            return null
        }

        return SimpleTooltipLineMarkerInfo(
            element,
            tooltip = "Migration already ran",
            icon = DatabaseIcons.ToolwindowDatabaseChanges,
        )
    }
}
