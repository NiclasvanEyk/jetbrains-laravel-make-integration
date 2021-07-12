package com.niclas_van_eyk.laravel_make_integration.common.php.documentation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PhpDocCommentTest {
    @Test
    fun `it parses an empty comment`() {
        val comment = PhpDocComment("""
            /**
             *
             */
        """.trimIndent())

        assertNull(comment.summary)
        assertNull(comment.description)
    }

    @Test
    fun `it parses a comment with simple summary and description`() {
        val comment = PhpDocComment("""
            /**
             * This is my summary.
             * This is my description.
             */
        """.trimIndent())

        assertEquals("This is my summary.", comment.summary)
        assertEquals("This is my description.", comment.description)
    }

    @Test
    fun `it parses a comment with simple summary and description divided by blank lines`() {
        val comment = PhpDocComment("""
            /**
             * This is my summary.
             *
             * This is my description.
             */
        """.trimIndent())

        assertEquals("This is my summary.", comment.summary)
        assertEquals("This is my description.", comment.description)
    }

    @Test
    fun `it parses a comment with summary and multi-line description`() {
        val comment = PhpDocComment("""
            /**
             * This is my summary.
             *
             * This is my description.
             *
             * This is also part of my description.
             */
        """.trimIndent())

        assertEquals("This is my summary.", comment.summary)
        assertEquals("""
            This is my description.

            This is also part of my description.
        """.trimIndent(), comment.description)
    }
}
