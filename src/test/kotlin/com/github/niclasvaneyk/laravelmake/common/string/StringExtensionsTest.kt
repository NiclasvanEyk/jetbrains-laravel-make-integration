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

        val withDockerStartupMessages = """
        Could not extract JSON while introspecting. Source:

        [+] Running 5/0

         ⠿ Container cv-app-selenium-1     Running 0.0s

         ⠿ Container cv-app-mailhog-1      Running 0.0s

         ⠿ Container cv-app-redis-1        Running 0.0s

         ⠿ Container cv-app-mysql-1        Running 0.0s

         ⠿ Container cv-app-meilisearch-1  Running 0.0s

        [{"domain":null,"method":"GET|HEAD","uri":"\/","name":null,"action":"Closure","middleware":["web"]},{"domain":null,"method":"POST","uri":"_ignition\/execute-solution","name":"ignition.executeSolution","action":"Spatie\\LaravelIgnition\\Http\\Controllers\\ExecuteSolutionController","middleware":["Spatie\\LaravelIgnition\\Http\\Middleware\\RunnableSolutionsEnabled"]},{"domain":null,"method":"GET|HEAD","uri":"_ignition\/health-check","name":"ignition.healthCheck","action":"Spatie\\LaravelIgnition\\Http\\Controllers\\HealthCheckController","middleware":["Spatie\\LaravelIgnition\\Http\\Middleware\\RunnableSolutionsEnabled"]},{"domain":null,"method":"POST","uri":"_ignition\/update-config","name":"ignition.updateConfig","action":"Spatie\\LaravelIgnition\\Http\\Controllers\\UpdateConfigController","middleware":["Spatie\\LaravelIgnition\\Http\\Middleware\\RunnableSolutionsEnabled"]},{"domain":null,"method":"GET|HEAD","uri":"api\/user","name":null,"action":"Closure","middleware":["api","App\\Http\\Middleware\\Authentic
        ate:sanctum"]},{"domain":null,"method":"GET|HEAD","uri":"dashboard","name":"dashboard","action":"Closure","middleware":["web","App\\Http\\Middleware\\Authenticate:sanctum","Laravel\\Jetstream\\Http\\Middleware\\AuthenticateSession","Illuminate\\Auth\\Middleware\\EnsureEmailIsVerified"]},{"domain":null,"method":"GET|HEAD","uri":"forgot-password","name":"password.request","action":"Laravel\\Fortify\\Http\\Controllers\\PasswordResetLinkController@create","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"POST","uri":"forgot-password","name":"password.email","action":"Laravel\\Fortify\\Http\\Controllers\\PasswordResetLinkController@store","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"GET|HEAD","uri":"login","name":"login","action":"Laravel\\Fortify\\Http\\Controllers\\AuthenticatedSessionController@create","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"POST","
        uri":"login","name":null,"action":"Laravel\\Fortify\\Http\\Controllers\\AuthenticatedSessionController@store","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web","Illuminate\\Routing\\Middleware\\ThrottleRequests:login"]},{"domain":null,"method":"POST","uri":"logout","name":"logout","action":"Laravel\\Fortify\\Http\\Controllers\\AuthenticatedSessionController@destroy","middleware":["web"]},{"domain":null,"method":"GET|HEAD","uri":"register","name":"register","action":"Laravel\\Fortify\\Http\\Controllers\\RegisteredUserController@create","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"POST","uri":"register","name":null,"action":"Laravel\\Fortify\\Http\\Controllers\\RegisteredUserController@store","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"POST","uri":"reset-password","name":"password.update","action":"Laravel\\Fortify\\Http\\Controllers\\NewPasswordController@store","middleware":["w
        eb","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"GET|HEAD","uri":"reset-password\/{token}","name":"password.reset","action":"Laravel\\Fortify\\Http\\Controllers\\NewPasswordController@create","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"GET|HEAD","uri":"sanctum\/csrf-cookie","name":"sanctum.csrf-cookie","action":"Laravel\\Sanctum\\Http\\Controllers\\CsrfCookieController@show","middleware":["web"]},{"domain":null,"method":"GET|HEAD","uri":"two-factor-challenge","name":"two-factor.login","action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorAuthenticatedSessionController@create","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web"]},{"domain":null,"method":"POST","uri":"two-factor-challenge","name":null,"action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorAuthenticatedSessionController@store","middleware":["web","App\\Http\\Middleware\\RedirectIfAuthenticated:web","Illuminate\\Routing\\Middleware\\
        ThrottleRequests:two-factor"]},{"domain":null,"method":"DELETE","uri":"user","name":"current-user.destroy","action":"Laravel\\Jetstream\\Http\\Controllers\\Inertia\\CurrentUserController@destroy","middleware":["web","App\\Http\\Middleware\\Authenticate:sanctum","Laravel\\Jetstream\\Http\\Middleware\\AuthenticateSession"]},{"domain":null,"method":"GET|HEAD","uri":"user\/confirm-password","name":null,"action":"Laravel\\Fortify\\Http\\Controllers\\ConfirmablePasswordController@show","middleware":["web","App\\Http\\Middleware\\Authenticate:web"]},{"domain":null,"method":"POST","uri":"user\/confirm-password","name":"password.confirm","action":"Laravel\\Fortify\\Http\\Controllers\\ConfirmablePasswordController@store","middleware":["web","App\\Http\\Middleware\\Authenticate:web"]},{"domain":null,"method":"GET|HEAD","uri":"user\/confirmed-password-status","name":"password.confirmation","action":"Laravel\\Fortify\\Http\\Controllers\\ConfirmedPasswordStatusController@show","middleware":["web","App\\Http\\Middleware\\Au
        thenticate:web"]},{"domain":null,"method":"POST","uri":"user\/confirmed-two-factor-authentication","name":"two-factor.confirm","action":"Laravel\\Fortify\\Http\\Controllers\\ConfirmedTwoFactorAuthenticationController@store","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]},{"domain":null,"method":"DELETE","uri":"user\/other-browser-sessions","name":"other-browser-sessions.destroy","action":"Laravel\\Jetstream\\Http\\Controllers\\Inertia\\OtherBrowserSessionsController@destroy","middleware":["web","App\\Http\\Middleware\\Authenticate:sanctum","Laravel\\Jetstream\\Http\\Middleware\\AuthenticateSession"]},{"domain":null,"method":"PUT","uri":"user\/password","name":"user-password.update","action":"Laravel\\Fortify\\Http\\Controllers\\PasswordController@update","middleware":["web","App\\Http\\Middleware\\Authenticate:web"]},{"domain":null,"method":"GET|HEAD","uri":"user\/profile","name":"profile.show","action":"Laravel\\Jetstream\\Http\\Controllers\\Ine
        rtia\\UserProfileController@show","middleware":["web","App\\Http\\Middleware\\Authenticate:sanctum","Laravel\\Jetstream\\Http\\Middleware\\AuthenticateSession"]},{"domain":null,"method":"PUT","uri":"user\/profile-information","name":"user-profile-information.update","action":"Laravel\\Fortify\\Http\\Controllers\\ProfileInformationController@update","middleware":["web","App\\Http\\Middleware\\Authenticate:web"]},{"domain":null,"method":"DELETE","uri":"user\/profile-photo","name":"current-user-photo.destroy","action":"Laravel\\Jetstream\\Http\\Controllers\\Inertia\\ProfilePhotoController@destroy","middleware":["web","App\\Http\\Middleware\\Authenticate:sanctum","Laravel\\Jetstream\\Http\\Middleware\\AuthenticateSession"]},{"domain":null,"method":"POST","uri":"user\/two-factor-authentication","name":"two-factor.enable","action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorAuthenticationController@store","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword
        "]},{"domain":null,"method":"DELETE","uri":"user\/two-factor-authentication","name":"two-factor.disable","action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorAuthenticationController@destroy","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]},{"domain":null,"method":"GET|HEAD","uri":"user\/two-factor-qr-code","name":"two-factor.qr-code","action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorQrCodeController@show","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]},{"domain":null,"method":"GET|HEAD","uri":"user\/two-factor-recovery-codes","name":"two-factor.recovery-codes","action":"Laravel\\Fortify\\Http\\Controllers\\RecoveryCodeController@index","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]},{"domain":null,"method":"POST","uri":"user\/two-factor-recovery-codes","name":null,"action":"Laravel\\Fortify\\Http\\Controllers\\R
        ecoveryCodeController@store","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]},{"domain":null,"method":"GET|HEAD","uri":"user\/two-factor-secret-key","name":"two-factor.secret-key","action":"Laravel\\Fortify\\Http\\Controllers\\TwoFactorSecretKeyController@show","middleware":["web","App\\Http\\Middleware\\Authenticate:web","Illuminate\\Auth\\Middleware\\RequirePassword"]}]

        Done!
        """.trimIndent()

        for (output in listOf(withXdebugMessages, withoutXdebugMessagesButWithRandomNewLines, withDockerStartupMessages)) {
            val json = output.containedJson()
            val routes = GsonBuilder()
                .create()
                .fromJson(json, Array<RouteListEntry>::class.java)
                .toList()

            assertTrue(routes.isNotEmpty())
        }
    }
}
