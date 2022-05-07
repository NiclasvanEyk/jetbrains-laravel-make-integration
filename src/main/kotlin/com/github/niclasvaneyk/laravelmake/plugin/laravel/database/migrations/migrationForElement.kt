package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass
import java.io.File

/**
 * If the element represents a class definition in a migration file,
 * it returns the corresponding migration object from the
 * [MigrationService].
 */
val PsiElement.migration: Migration? get() {
    if (containingFile?.parent?.name != "migrations") return null
    if (this !is PhpClass) return null

    val migrations = project.service<MigrationService>()
    val inferredName = File(containingFile.name).nameWithoutExtension
    return migrations.current.find { it.name == inferredName}
}