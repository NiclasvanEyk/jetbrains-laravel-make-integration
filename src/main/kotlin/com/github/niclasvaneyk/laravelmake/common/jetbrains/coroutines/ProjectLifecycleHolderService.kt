package com.github.niclasvaneyk.laravelmake.common.jetbrains.coroutines

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

val Project.lifecycleScope: CoroutineScope
    get() = service<ProjectLifecycleHolderService>()

@Service(Service.Level.PROJECT)
internal class ProjectLifecycleHolderService : CoroutineScope, Disposable {
    override val coroutineContext = SupervisorJob() + CoroutineName(this::class.qualifiedName!!)

    override fun dispose() = cancel("Disposing ${this::class.qualifiedName}")
}