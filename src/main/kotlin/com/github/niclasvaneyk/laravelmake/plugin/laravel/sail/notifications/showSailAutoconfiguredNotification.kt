package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.notifications

import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailNotificationGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.php.phpunit.PhpUnitLocalRunConfiguration
import java.io.File

fun showSailAutoconfiguredNotification(application: LaravelApplication) {
//    val exampleActions = mutableListOf<AnAction>()
//
//    if (File(application.paths.path("phpunit.xml")).isFile) {
//        exampleActions.add(object: AnAction() {
//            override fun actionPerformed(e: AnActionEvent) {
//                PhpUnitLocalRunConfiguration()
//            }
//        })
//    }

    SailNotificationGroup
        .info(
            title = "Laravel Sail Setup Completed",
            content = """
                    The IDE will now use Laravel Sail for running PHP, Node 
                    and NPM related tasks! 
                """.trimIndent()
            // TODO: At some point I'd like to add two example actions here, so the
            //       user can click on "start containers", "execute tests" or
            //       "compile frontend assets" and thereby can setup those run
            //       configurations in an easy way
            // Try running one of the options below to get started.
        )
        .notify(application.project)
}