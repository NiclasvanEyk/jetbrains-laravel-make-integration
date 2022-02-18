package com.github.niclasvaneyk.laravelmake.common.php.run

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Key

/**
 * Records the log output of a [PHPRunner].
 */
class PHPScriptRunListener : ProcessListener {
    val texts = ArrayList<String>()

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        println(event.text)
        texts.add(event.text)
    }

    override fun processTerminated(event: ProcessEvent) {
        println("Terminated with exit code ${event.exitCode}!")
    }

    @Suppress("EmptyFunctionBlock")
    override fun startNotified(event: ProcessEvent) {
    }
}
