package com.niclas_van_eyk.laravel_make_integration.laravel

import com.google.gson.GsonBuilder
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.jetbrains.php.run.PhpEditInterpreterExecutionException
import com.niclas_van_eyk.laravel_make_integration.ide.IdeAdapter
import com.niclas_van_eyk.laravel_make_integration.ide.jetbrains.PluginNotifications
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.LaravelConsoleApplication
import com.niclas_van_eyk.laravel_make_integration.laravel.artisan.RouteListEntry
import com.niclas_van_eyk.laravel_make_integration.run.InterpreterInference
import com.niclas_van_eyk.laravel_make_integration.run.PHPScriptRun

class CommandRunInfo(
    val namespace: String,
    val command: String?,
    val options: List<String> = emptyList()
)

class LaravelApplicationIntrospecter(
    private val laravelProject: LaravelProject,
    private val project: Project
) {
    fun fetchCommandInfo(onSuccess: (app: LaravelConsoleApplication) -> Unit) {
        val jsonFormatOption = "--format=json"

        receiveCommandOutputAsJson(
            "Scanning Artisan commands",
            CommandRunInfo("list", null, listOf(jsonFormatOption))
        ) { output ->
            val json = output
                // For some reason an empty argument list gets serialized
                // to an empty array instead of an empty object. Maybe
                // because of associative arrays in php are kinda the
                // same as JSON objects.
                .replace(Regex("\"options\":\\[\\]]"), "\"options\":{}")
                .replace(Regex("\"arguments\":\\[\\]]"), "\"arguments\":{}")
                // These are getting added by Docker I think
                .replace(Regex("\n"), "")

            val application = GsonBuilder()
                .create()
                .fromJson(json, LaravelConsoleApplication::class.java)

            onSuccess(application)
        }
    }

    fun fetchConfigInfo(onSuccess: (result: Any) -> Unit) {
        receiveCommandOutputAsJson(
            "Scanning Laravel config",
            CommandRunInfo("tinker", null, listOf("--execute", "echo json_encode(config()->all())"))
        ) { output ->
            onSuccess(GsonBuilder().create().fromJson(output, Any::class.java))
        }
    }

    fun fetchRouteInfo(onSuccess: (app: List<RouteListEntry>) -> Unit) {
        receiveCommandOutputAsJson(
            "Scanning Laravel routes",
            CommandRunInfo("route", "list", listOf("--json"))
        ) { output ->
            val routes = GsonBuilder()
                .create()
                .fromJson(output, Array<RouteListEntry>::class.java)

            onSuccess(routes.toList())
        }
    }

    private fun receiveCommandOutputAsJson(title: String, commandRunInfo: CommandRunInfo, onSuccess: (result: String) -> Unit) {
        if (! InterpreterInference(project).hasInterpreter()) {
            // We cannot execute anything here, so we need to skip it instead of throwing
            // an exception as https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/issues/28
            // showed
            return
        }

        ProgressManager.getInstance().run(object: Task.Backgroundable(project, title, true, ALWAYS_BACKGROUND) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                var commandScanResult: PHPScriptRun.Result

                try {
                    commandScanResult = laravelProject.artisan.execute(
                            project,
                            commandRunInfo.namespace,
                            commandRunInfo.command,
                            commandRunInfo.options
                    )
                } catch (e: PhpEditInterpreterExecutionException) {
                    var message = "Could not connect to the configured remote interpreter."

                    if (e.message?.contains("Docker") == true) {
                        message += " Maybe you need to start your Docker daemon?"
                    }

                    commandScanResult = PHPScriptRun.Result(false, arrayListOf(message))
                }

                if (commandScanResult.wasFailure) {
                    indicator.stop()
                    return
                }

                // First the command line arguments are logged. If Docker is used, they contain a '[', which
                // trips up our magnificent strategy for detecting the JSON output
                val output = commandScanResult.log.substringAfter("\n")
                val beginOfJson = output.indexOfAny(listOf("{", "["))
                val endOfJson = output.lastIndexOfAny(listOf("]", "}"))

                if (beginOfJson == -1 || endOfJson == -1) {
                    PluginNotifications.error(
                        "Something went wrong while trying to parse the JSON output of '$title'!",
                        "No JSON found"
                    ).notify(project)
                    return
                }

                val json = output.substring(beginOfJson, endOfJson + 1).trim()

                onSuccess(json)
            }
        })
    }
}