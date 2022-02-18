package com.github.niclasvaneyk.laravelmake.common.string

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class StringExtensionsTest {
    @Test
    fun paragraphs() {
        val article = """
            Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam 
            nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam 
            erat, sed diam voluptua. At vero eos et accusam et justo duo dolores
            et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
            Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur 
            sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore 
            et dolore magna aliquyam erat, sed diam voluptua. At vero eos et 
            accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren,
            no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum 
            dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
             tempor invidunt ut labore et dolore magna aliquyam erat, sed diam 
             voluptua. 

            Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse 
            molestie consequat, vel illum dolore eu feugiat nulla facilisis at 
            vero eros et accumsan et iusto odio dignissim qui blandit praesent 
            luptatum zzril delenit augue duis dolore te feugait nulla facilisi. 

            Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper
            suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis 
            autem vel eum iriure dolor in hendrerit in vulputate velit esse 
            molestie consequat, vel illum dolore eu feugiat nulla facilisis at 
            vero eros et accumsan et iusto odio dignissim qui blandit praesent 
            luptatum zzril delenit augue duis dolore te feugait nulla facilisi. 
        """.trimIndent()

        assertEquals(3, article.paragraphs().count())
    }
}
