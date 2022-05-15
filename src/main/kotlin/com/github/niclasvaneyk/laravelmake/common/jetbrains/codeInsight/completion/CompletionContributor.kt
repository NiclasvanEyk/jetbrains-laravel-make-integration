package com.github.niclasvaneyk.laravelmake.common.jetbrains.codeInsight.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.codeInsight.completion.CompletionContributor as BaseCompletionContributor

abstract class CompletionContributor: BaseCompletionContributor() {
    fun contribute(
        pattern: ElementPattern<PsiElement>,
        provider: CompletionProvider,
    ) = extend(CompletionType.BASIC, pattern, provider)
}