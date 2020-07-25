package com.niclas_van_eyk.laravel_make_integration.run

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Key

class PHPScriptRunListener: ProcessListener {
    val texts = ArrayList<String>()

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        println(event.text)
        texts.add(event.text)
    }

    override fun processTerminated(event: ProcessEvent) {
        println("Terminated with exit code ${event.exitCode}!")
    }

    override fun startNotified(event: ProcessEvent) {
    }
}