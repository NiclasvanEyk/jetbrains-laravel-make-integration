package com.niclas_van_eyk.laravel_make_integration.laravel

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.config.interpreters.PhpInterpretersManager
import com.niclas_van_eyk.laravel_make_integration.run.PHPScriptRunner
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import com.niclas_van_eyk.laravel_make_integration.laravel.Project as LaravelProject

class Artisan(private val base: VirtualFile) {
    val basePath: String
        get() = base.canonicalPath ?: ""

    val binaryPath: String
        get() = "$basePath/$relativeBinaryPath"

    val relativeBinaryPath: String
        get() = "artisan"

    val exists: Boolean
        get() = File(binaryPath).exists()

    fun make(subCommand: String, params: Iterable<String>, project: Project): CompletableFuture<Boolean> {
        return execute("make", subCommand, params, project)
    }

    fun execute(namespace: String, subCommand: String, subCommandParams: Iterable<String>, project: Project): CompletableFuture<Boolean> {
        val params = mutableListOf(binaryPath, "$namespace:$subCommand")
        params.addAll(subCommandParams)

        PHPScriptRunner().run(binaryPath, listOf("inspire"), project)

        val cli = GeneralCommandLine(params)

        cli.charset = Charset.forName("UTF-8")
        cli.workDirectory = File(basePath)

        val process = cli.createProcess()
        process.waitFor(10, TimeUnit.SECONDS)
        return process.onExit().thenApply {
            if (it.exitValue() != 0) {
                val group = NotificationGroup("laravel_make_integration_errors", NotificationDisplayType.STICKY_BALLOON, true)
                val notification = group.createNotification(
                    "Error running make:$subCommand",
                    NotificationType.ERROR
                )
                notification.isImportant = true

                notification.notify(project)

                return@thenApply false
            }

            return@thenApply true
        }
    }
}