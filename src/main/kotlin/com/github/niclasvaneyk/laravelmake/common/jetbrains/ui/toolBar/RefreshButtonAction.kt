package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import io.reactivex.rxjava3.core.Observable

open class RefreshButtonAction(
    val text: String = "Refresh",
    val description: String,
    isRefreshing: Observable<Boolean>,
    val refresh: (e: AnActionEvent) -> Unit,
) : AnAction(text, description, AllIcons.Actions.Refresh) {
    var isRefreshing = false

    init {
        isRefreshing.subscribe { this.isRefreshing = it }
    }

    override fun actionPerformed(e: AnActionEvent) {
        refresh(e)
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = !isRefreshing
    }
}
