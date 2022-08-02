package com.github.niclasvaneyk.laravelmake.common.laravel

import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.extends
import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.isPhpIdentifier
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

object Livewire {
    const val COMPONENT_FQN = "\\Livewire\\Component"
}

/**
 * Only return methods and their identifiers.
 */
fun List<PsiElement>.livewireComponentClasses(): List<Pair<PsiElement, PhpClass>> = this
    .filter { it.isPhpIdentifier() && it.parent is PhpClass && (it.parent as PhpClass).extends(Livewire.COMPONENT_FQN) }
    .map { Pair(it, it.parent as PhpClass) }
