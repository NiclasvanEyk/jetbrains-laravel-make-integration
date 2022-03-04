package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.seeders

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php.basename
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.codeInsight.ControllerActionLineMarker
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement

class RunSeederLineMarker(
    element: PsiElement,
    val seeder: String,
): LineMarkerInfo<PsiElement>(
    element,
    element.textRange,
    AllIcons.Actions.Execute,
    { tooltip(seeder) },
    null,
    GutterIconRenderer.Alignment.LEFT,
    { tooltip(seeder) },
)  {
    companion object {
        fun tooltip(seeder: String) = "Run '${basename(seeder)}'"
    }
}