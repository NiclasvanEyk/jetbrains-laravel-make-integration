package com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi

import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass

/**
 * Whether the element is actually an identifier.
 *
 * This is very useful for [LineMarkerProviderDescriptor]s, as is it considered
 * a best-practise to only decorate the identifiers, and not e.g. an entire
 * [PhpClass] object spanning multiple lines.
 */
fun PsiElement.isPhpIdentifier() = elementType == PhpTokenTypes.IDENTIFIER

/**
 * Whether the class extends the given [fqn].
 *
 * Note that in general [fqn] should start with a '\', since this is what is
 * returned by [PhpClass.getFQN].
 */
fun PhpClass.extends(fqn: String): Boolean = superClasses.any { it.fqn == fqn }

/**
 * Only return methods and their identifiers.
 */
fun List<PsiElement>.phpMethods(): List<Pair<PsiElement, Method>> = this
    .filter { it.isPhpIdentifier() && it.parent is Method }
    .map { identifier -> Pair(identifier, identifier.parent as Method) }
