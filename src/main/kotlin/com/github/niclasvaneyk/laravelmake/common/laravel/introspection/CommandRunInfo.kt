package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

class CommandRunInfo(
    val namespace: String,
    val command: String?,
    val options: List<String> = emptyList()
)
