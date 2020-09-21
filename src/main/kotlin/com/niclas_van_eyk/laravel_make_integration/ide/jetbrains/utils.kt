package com.niclas_van_eyk.laravel_make_integration.ide.jetbrains

import com.intellij.concurrency.SensitiveProgressWrapper
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.Computable
import java.util.concurrent.Callable
import java.util.concurrent.Future

fun <R> runWithProgress(thingToRun: Computable<R>, label: String? = null): Future<R> {
    val progressManager = ProgressManager.getInstance()
    val mainIndicator = progressManager.progressIndicator
    val indicator = if (mainIndicator != null) SensitiveProgressWrapper(mainIndicator)
                    else EmptyProgressIndicator()

    if (label != null) {
        indicator.text = label
    }

    return ApplicationManager.getApplication().executeOnPooledThread(Callable<R> {
        progressManager.runProcess(thingToRun, indicator)
    })
}