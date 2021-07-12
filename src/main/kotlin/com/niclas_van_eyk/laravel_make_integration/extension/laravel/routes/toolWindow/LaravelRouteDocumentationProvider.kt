package com.niclas_van_eyk.laravel_make_integration.extension.laravel.routes.toolWindow

import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.documentation.PhpDocSource
import com.jetbrains.php.lang.documentation.PhpDocumentationProvider
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.php.documentation.PhpDocMarkdownUtil
import com.niclas_van_eyk.laravel_make_integration.common.php.documentation.PhpDocComment
import kotlinx.html.*
import kotlinx.html.stream.createHTML

class LaravelRouteDocumentationProvider {
    fun generateDoc(element: PsiElement): String {
        val source = docSourceFor(element) ?: return ""

        return generateDoc(source)
    }

    fun generateDoc(source: PhpDocSource): String {
        val docComment = PhpDocComment(source.docComment?.text ?: "")
        val descriptionHtml = PhpDocMarkdownUtil().markdown2Html(
            docComment.description ?: ""
        )

        return createHTML()
            .div {
                h2 { +(docComment.summary ?: "") }
                unsafe { raw(descriptionHtml) }
        }
    }

    private fun docSourceFor(element: PsiElement): PhpDocSource? {
        return PhpDocumentationProvider.getDocSourceFor(element, element)
    }
}
