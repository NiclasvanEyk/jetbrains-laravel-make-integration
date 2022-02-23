package com.github.niclasvaneyk.laravelmake.common.jetbrains.progress

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class ProgressBarBuilder(private val project: Project) {
    fun indeterminate(
        title: String,
        onError: (throwable: Throwable) -> Unit,
        callback: (indicator: ProgressIndicator) -> Unit,
    ) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(
            project,
            title,
            true,
            ALWAYS_BACKGROUND,
        ) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                callback(indicator)
            }

            override fun onThrowable(error: Throwable) {
                onError(error)
            }
        })
    }
}
