package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.php.run.PHPRunner
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.rd.util.withBackgroundProgressContext
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * Maps the results of an introspection command (e.g. artisan route:list) to an
 * observable, so it can be easily refreshed and read.
 */
abstract class CommandBasedIntrospecter<T>(
    private val artisan: Artisan,
    private val progressBar: ProgressBarBuilder,
) {
    val staleDataDetector = StaleDataDetector()

    val introspectionStateSource: BehaviorSubject<IntrospectionState<T>> = BehaviorSubject
        .createDefault(InitialState())

    val introspectionState: IntrospectionSubject<T> = introspectionStateSource
        .doAfterNext { staleDataDetector.markAsRefreshed() }

    private val lifecycle = IntrospectionLifecycle(introspectionStateSource)
    private var isRefreshing = false

    /**
     * Text that describes what is being introspected.
     */
    abstract val description: String

    /**
     * The command to be run
     */
    abstract val command: CommandRunInfo

    /**
     * Creates the structured data from the cleaned command output.
     */
    protected abstract fun onCommandOutput(output: String, publish: (result: T) -> Unit)

    /**
     * Logic which actually runs the introspection.
     */
    protected open fun introspect(
        onData: (result: T) -> Unit,
        onError: (message: String, exception: Throwable?) -> Unit,
    ) {
        val result: PHPRunner.Result
        try {
            result = artisan.command(
                command.namespace,
                command.command,
                command.options,
            )
        } catch (exception: Throwable) {
            onError(exception.message ?: "", exception)
            return
        }

        if (result.wasFailure) {
            // TODO: Better message or exception?
            onError(result.log, null)
            return
        }

        // First the command line arguments are logged, so we skip those.
        val output = result.log.substringAfter("\n")

        onCommandOutput(output, onData)
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        lifecycle.onLoadingStarted()

        runBackgroundableTask(description) { indicator ->
            indicator.isIndeterminate = true
            try {
                introspect({ result ->
                    if (indicator.isRunning) indicator.stop()
                    lifecycle.onData(result)
                }) { message, exception ->
                    if (indicator.isRunning) indicator.stop()
                    lifecycle.onError(message, exception)
                    if (exception != null) {
//                        logger<CommandBasedIntrospecter<T>>().error(exception)
                    }
                }
            } catch (exception: Throwable) {
                if (indicator.isRunning) indicator.stop()
//                logger<CommandBasedIntrospecter<T>>().error(exception)
                lifecycle.onError(exception.message ?: "", exception)
            } finally {
                isRefreshing = false
            }
        }
    }
}
