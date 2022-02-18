package com.github.niclasvaneyk.laravelmake.common.laravel.commands

import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.Option

val Collection<Option>.withoutGlobalOptions: List<Option>
    get() = filter { !GLOBAL_OPTIONS.contains(it.name) }

val GLOBAL_OPTIONS = setOf(
    "--help",
    "--quiet",
    "--verbose",
    "--version",
    "--ansi",
    "--no-ansi",
    "--no-interaction",
    "--env",
)
