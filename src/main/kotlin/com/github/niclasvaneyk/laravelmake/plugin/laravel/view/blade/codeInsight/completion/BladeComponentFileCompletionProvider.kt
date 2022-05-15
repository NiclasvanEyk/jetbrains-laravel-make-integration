package com.github.niclasvaneyk.laravelmake.plugin.laravel.view.blade.codeInsight.completion

import com.github.niclasvaneyk.laravelmake.common.jetbrains.codeInsight.completion.CompletionProvider
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

class BladeComponentFileCompletionProvider: CompletionProvider() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        val app = parameters.originalFile.project.laravel() ?: return
        parameters.
    }
}