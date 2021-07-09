package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow.toolBar

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.niclas_van_eyk.laravel_make_integration.extension.laravel.make.jetbrains.actions.MakeControllerAction

class CreateControllerAction: AnAction(
    "Create Controller",
    "Create a new controller using php artisan make:controller",
    AllIcons.General.Add
) {
    override fun actionPerformed(e: AnActionEvent) {
        MakeControllerAction().actionPerformed(e)
    }
}
