package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.PsiElement
import icons.DatabaseIcons

/**
 * Adds a little button that runs `artisan migrate` when we can detect that
 * the currently opened migration file has not been run yet.
 */
class MigrationRunLineMarkerContributor: RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        val migration = element.migration ?: return null
        if (migration.ran) return null // Handled elsewhere

        return Info(
            DatabaseIcons.RunDatabaseScript,
            ExecutorAction.getActions(),
            { "artisan migrate" }
        )
    }
}