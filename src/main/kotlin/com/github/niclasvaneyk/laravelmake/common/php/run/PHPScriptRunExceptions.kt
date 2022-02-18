package com.github.niclasvaneyk.laravelmake.common.php.run

import com.intellij.execution.ExecutionException
import com.jetbrains.php.run.PhpEditInterpreterExecutionException

open class PHPScriptRunException(message: String, cause: Throwable) :
    RuntimeException(message, cause)

open class InterpreterScriptRunException(message: String, cause: Throwable) :
    PHPScriptRunException(message, cause)

class NoProjectInterpreterException(
    cause: ExecutionException
) : InterpreterScriptRunException(
    "You need to set up a PHP interpreter for this project!",
    cause
)

class InvalidInterpreterException(
    cause: ExecutionException
) : InterpreterScriptRunException(
    "It seems like the PHP interpreter for this project is not valid.",
    cause
)

class ProjectInterpreterNotFoundException(
    cause: ExecutionException,
) : InterpreterScriptRunException(
    "It seems like the configured php interpreter for this project cannot be " +
        "found.",
    cause
)

class RemoteInterpreterNotAvailableException(
    cause: PhpEditInterpreterExecutionException,
) : InterpreterScriptRunException(
    "Could not connect to the configured remote interpreter.",
    cause
)

class DockerNotStartedException(
    cause: PhpEditInterpreterExecutionException,
) : InterpreterScriptRunException(
    "Could not connect to the configured remote interpreter. " +
        " Maybe you need to start your Docker daemon?",
    cause
)
