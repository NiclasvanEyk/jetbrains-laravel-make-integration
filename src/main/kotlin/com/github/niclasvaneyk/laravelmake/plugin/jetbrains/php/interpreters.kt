package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php

import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.settings.settings
import com.intellij.openapi.project.Project
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import kotlinx.collections.immutable.toImmutableList

val Project.phpInterpreters: List<PhpInterpreter>
    get() = PhpInterpretersManagerImpl.getInstance(this).interpreters.toImmutableList()

/**
 * The PHP interpreter as configured by the user (if it exists).
 */
val Project.desiredPhpInterpreter: PhpInterpreter?
    get() {
        val name = settings.interpreterId ?: return null
        return phpInterpreters.find { it.name == name }
    }
