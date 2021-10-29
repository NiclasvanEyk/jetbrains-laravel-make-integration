package com.niclas_van_eyk.laravel_make_integration.common.laravel.commands

import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.introspection.Option

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
