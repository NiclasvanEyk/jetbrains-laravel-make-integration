package com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection

import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.progress.ProgressBarBuilder
import com.niclas_van_eyk.laravel_make_integration.common.laravel.Artisan
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * Maps the results of a introspection command (e.g. artisan route:list) to an
 * observable, so it can be easily refreshed and read.
 */
abstract class CommandBasedIntrospecter<T>(
    private val artisan: Artisan,
    private val progressBar: ProgressBarBuilder,
) {
    val staleDataDetector = StaleDataDetector()

    val introspectionStateSource = BehaviorSubject
        .createDefault<IntrospectionState<T>?>(InitialState())

    val introspectionState: IntrospectionSubject<T> = introspectionStateSource
        .doAfterNext {
            staleDataDetector.markAsRefreshed()
        }

    private val lifecycle = IntrospectionLifecycle(introspectionStateSource)

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
    protected abstract fun marshalResult(output: String): T

    /**
     * Logic which actually runs the introspection.
     */
    protected open fun introspect(
        onData: (result: T) -> Unit,
        onError: (message: String, cause: Throwable?) -> Unit,
    ) {
        val result = artisan.command(
            command.namespace,
            command.command,
            command.options,
        )

        if (result.wasFailure) {
            // TODO: Better message or exception?
            onError(result.log, null)
            return
        }

        // First the command line arguments are logged, so we skip those.
        val output = result.log.substringAfter("\n")
        val cleanOutput = prepareCommandOutput(output)

        if (cleanOutput == null) {
            // TODO: Better message or exception and somehow detect empty
            //       responses?
            onError("Introspection yielded no output!", null)
            return
        }

        val marshalledObjects = marshalResult(cleanOutput)

        onData(marshalledObjects)
    }

    /**
     * Clean command output, so it can be passed to [marshalResult].
     *
     * Parses JSON by default, but can be overridden when the command uses
     * another output format, e.g. a table.
     */
    protected open fun prepareCommandOutput(output: String): String? {
        return parseJsonFromOutput(output)
    }

    private fun parseJsonFromOutput(output: String): String? {
        val beginOfJson = output.indexOfAny(listOf("{", "["))
        val endOfJson = output.lastIndexOfAny(listOf("]", "}"))

        if (beginOfJson == -1 || endOfJson == -1) return null

        return output.substring(beginOfJson, endOfJson + 1).trim()
    }

    fun refresh() {
        lifecycle.onLoadingStarted()

        progressBar.indeterminate(description) { indicator ->
            indicator.isIndeterminate = true

            try {
                introspect(
                    { result ->
                        indicator.stop()
                        lifecycle.onData(result)
                    },
                    { message, cause ->
                        indicator.stop()
                        lifecycle.onError(message, cause)
                    }
                )
            } catch (exception: Throwable) {
                indicator.stop()
                lifecycle.onError(exception.localizedMessage, exception)
            }
        }
    }
}
