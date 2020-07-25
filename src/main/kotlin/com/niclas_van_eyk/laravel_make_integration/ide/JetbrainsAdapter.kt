package com.niclas_van_eyk.laravel_make_integration.ide

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.niclas_van_eyk.laravel_make_integration.LaravelMakeIntegrationBundle
import com.niclas_van_eyk.laravel_make_integration.actions.SubCommand
import com.niclas_van_eyk.laravel_make_integration.tryToOpenFile

class JetbrainsAdapter(
    private val project: Project,
    private val meta: SubCommand
): IdeAdapter {
    override fun openFile(path: String) {
        tryToOpenFile(project, path)
    }

    override fun getUserInput(initialValue: String?): String? {
        val initial = initialValue ?: ""

        val input = Messages.showInputDialog(
            project,
            meta.description,
            meta.capitalized,
            null,
                initial,
            null,
            // this way the cursor is at the end
            TextRange(initial.length, initial.length)
        )

        if (input.isNullOrEmpty() || input.trim() == initial.trim()) return null

        return input
    }

    override fun log(key: String, message: String) {
        LaravelMakeIntegrationBundle.message(key, message)
    }

    override fun notification(message: String) {

    }
}
