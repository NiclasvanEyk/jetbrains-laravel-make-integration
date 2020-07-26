package com.niclas_van_eyk.laravel_make_integration.run

class ScriptRunFailedException(val run: PHPScriptRunListener): Exception() {
    val output: String
        get() = run.texts.joinToString("\n")
}