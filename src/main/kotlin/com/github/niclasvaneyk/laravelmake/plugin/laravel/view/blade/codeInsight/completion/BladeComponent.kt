package com.github.niclasvaneyk.laravelmake.plugin.laravel.view.blade.codeInsight.completion

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

class BladeComponent(private val `class`: PhpClass) {
    companion object {
        fun fromFile(file: PsiFile): BladeComponent? {
//            file.children
        }
    }

    val viewProperties: List<Field> get() = `class`
        .fields
        .filter { it.modifier.isPublic }

    val viewMethods: List<Method> get() = `class`
        .methods
        .filter { !it.isStatic && it.modifier.isPublic }
}
