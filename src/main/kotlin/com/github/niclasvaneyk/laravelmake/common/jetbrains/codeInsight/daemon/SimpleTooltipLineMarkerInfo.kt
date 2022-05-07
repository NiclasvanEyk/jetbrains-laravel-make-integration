package com.github.niclasvaneyk.laravelmake.common.jetbrains.codeInsight.daemon

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import javax.swing.Icon

open class SimpleTooltipLineMarkerInfo(
    element: PsiElement,
    tooltip: String,
    icon: Icon
): LineMarkerInfo<PsiElement>(
    element,
    element.textRange,
    icon,
    { tooltip },
    null,
    GutterIconRenderer.Alignment.LEFT,
    { tooltip },
)