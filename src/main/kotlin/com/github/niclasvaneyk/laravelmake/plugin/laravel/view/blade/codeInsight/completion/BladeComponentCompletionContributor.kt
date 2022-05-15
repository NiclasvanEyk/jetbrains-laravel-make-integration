package com.github.niclasvaneyk.laravelmake.plugin.laravel.view.blade.codeInsight.completion

import com.github.niclasvaneyk.laravelmake.common.jetbrains.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.jetbrains.php.blade.BladeFileType
import com.jetbrains.php.blade.BladeLanguage
import com.jetbrains.php.lang.PhpLanguage

class BladeComponentCompletionContributor: CompletionContributor() {
    init {
        contribute(inBladeComponentFile(), BladeComponentFileCompletionProvider())
    }

    private fun inBladeViewFile() = PlatformPatterns
        .psiElement()
        .withLanguage(BladeLanguage.INSTANCE)

    private fun inBladeComponentFile() = PlatformPatterns
        .psiElement()
        .inVirtualFile(
            PlatformPatterns.virtualFile().ofType(BladeFileType.INSTANCE)
        )
        .withLanguage(PhpLanguage.INSTANCE)
}