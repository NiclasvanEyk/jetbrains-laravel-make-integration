package laravel_make_integration.laravel

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset

class Artisan(private val base: VirtualFile) {
    private val basePath: String
        get() = base.canonicalPath ?: ""

    private val binaryPath: String
        get() = "$basePath/artisan"

    val exists: Boolean
        get() = File(binaryPath).exists()

    fun make(subCommand: String, params: Iterable<String>): Boolean {
        return execute("make", subCommand, params)
    }

    fun execute(namespace: String, subCommand: String, subCommandParams: Iterable<String>): Boolean {
        val params = mutableListOf(binaryPath, "$namespace:$subCommand")
        params.addAll(subCommandParams)

        val cli = GeneralCommandLine(params)

        cli.charset = Charset.forName("UTF-8")
        cli.workDirectory = File(basePath)

        val process = cli.createProcess()
        process.waitFor()
        if (process.exitValue() != 0) {
            NotificationGroup("laravel_make_integration_errors", NotificationDisplayType.BALLOON)
                    .createNotification(
                            "Error running make:$subCommand",
                            IOUtils.toString(process.inputStream, Charset.defaultCharset()),
                            NotificationType.ERROR
                    ).notify(ProjectManager.getInstance().defaultProject)

            return false
        }

        return true
    }
}