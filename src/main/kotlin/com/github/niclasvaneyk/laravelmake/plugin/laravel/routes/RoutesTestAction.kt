package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes

import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandRunInfo
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.actionSystem.LaravelAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.LaravelApplication
import com.intellij.openapi.progress.runBackgroundableTask

fun Any.resource(path: String): String {
    return this.javaClass.classLoader.getResource(path).readText()
}

class RunCode(code: String): CommandRunInfo("tinker", null, listOf("--execute", code))

class RoutesTestAction: LaravelAction() {
    override fun run(application: LaravelApplication) {
        val code = resource("php/routes.php").removePrefix("<?php")
//        val command = RunCode("<<MAKEFORLARAVELEXECUTEDCODE\n$code\nMAKEFORLARAVELEXECUTEDCODE")
        val command = RunCode(code)

        runBackgroundableTask("Testing routes") {
            val result = application.artisan.command(command.namespace, command.command, command.options)
            print(result.log)
        }
    }
}