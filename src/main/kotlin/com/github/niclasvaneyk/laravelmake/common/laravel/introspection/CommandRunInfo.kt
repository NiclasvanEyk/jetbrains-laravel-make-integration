package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.LaravelMakeIntegrationBundle
import java.io.BufferedReader
import java.io.InputStreamReader


open class CommandRunInfo(
    val namespace: String,
    val command: String?,
    val options: List<String> = emptyList()
)

class RunCode(code: String): CommandRunInfo(
    "tinker",
    null,
    listOf("--execute", code),
)

