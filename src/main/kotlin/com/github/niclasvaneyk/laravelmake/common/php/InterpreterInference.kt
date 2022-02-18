package com.github.niclasvaneyk.laravelmake.common.php

import com.intellij.openapi.project.Project
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.github.niclasvaneyk.laravelmake.common.php.run.NoInterpreterSetException

class InterpreterInference(private val project: Project) {
    fun hasInterpreter(): Boolean {
        return inferConfiguredInterpreter() != null || inferLocalInterpreter() != null
    }

    fun inferInterpreter(): PhpInterpreter {
        val configuredInterpreter = inferConfiguredInterpreter()

        if (configuredInterpreter != null) {
            return configuredInterpreter
        }

        // It could be, that the user has not specified an interpreter.
        // If we find one locally, we will just use that and notify the
        // user, that they should specify an interpreter for the project,
        // so that the nasty message goes away.
        // We choose a local interpreter, because the remote ones could
        // be pointing to a server via ssh, so we do not be responsible
        // for any unwanted side-effects.
        val localInterpreter = inferLocalInterpreter()

        return localInterpreter ?: throw NoInterpreterSetException()
    }

    private fun inferConfiguredInterpreter(): PhpInterpreter? {
        return PhpProjectConfigurationFacade
            .getInstance(project)
            .interpreter
    }

    private fun inferLocalInterpreter(): PhpInterpreter? {
        return PhpInterpretersManagerImpl.getInstance(project)
            .interpreters
            .firstOrNull { !it.isRemote }
    }
}
