package com.github.niclasvaneyk.laravelmake.common.string

import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteListEntry
import com.google.gson.GsonBuilder
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun `json can be extracted from command outputs`() {
        val withXdebugMessages = """
        [22-Feb-2022 15:20:26 UTC] Xdebug: [Step Debug] Could not connect to debugging client. Tried: host.docker.internal:9003 (fallback through xdebug.client_host/xdebug.client_port) :-(

        [{"domain":null,"method":"GET|HEAD","uri":"api","name":null,"action":"Closure","middleware":["web"]},{"domain":null,"method":"GET|HEAD","uri":"api\/current","name":null,"action":"ApiController@current","middleware":["api","App\\Http\\Middleware\\Authenticate:sanctum"]}]

        [22-Feb-2022 15:20:26 UTC] Xdebug: [Step Debug] Could not connect to debugging client. Tried: host.docker.internal:9003 (fallback through xdebug.client_host/xdebug.client_port) :-(
        """.trimIndent()
        val withoutXdebugMessagesButWithRandomNewLines = """
        [{"domain":null,"method":"GET|HEAD","uri":"api","name":null,"action":"Closure","middleware":["web"]},{"dom
        ain":null,"method":"GET|HEAD","uri":"api\/current","name":null,"action":"ApiController@current","middleware":["api","App\\Http\\
        Middleware\\Authenticate:sanctum"]}]
        
        """.trimIndent()

        for (output in listOf(withXdebugMessages, withoutXdebugMessagesButWithRandomNewLines)) {
            val json = output.containedJson()
            val routes = GsonBuilder()
                .create()
                .fromJson(json, Array<RouteListEntry>::class.java)
                .toList()

            assertTrue(routes.isNotEmpty())
        }
    }
}
