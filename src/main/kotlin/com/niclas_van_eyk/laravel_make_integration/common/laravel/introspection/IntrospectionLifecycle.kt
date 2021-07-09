package com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * Base class that needs to be further matched to one of the sub-classes below
 * to extract useful contents.
 */
sealed class IntrospectionState<T>(val loading: Boolean)

/**
 * Initial state, when no data fetching has happened yet.
 */
class InitialState<T> : IntrospectionState<T>(false)

/**
 * Now we do not have data yet, but we are in the process of obtaining our
 * first batch of it.
 */
class InitialLoadingState<T> : IntrospectionState<T>(true)

/**
 * Now the loading has finished and we can use the obtained data.
 */
class LoadedState<T>(val result: T) : IntrospectionState<T>(false)

/**
 * Prior to this, we have already obtained some data, but we can still access
 * a stale version of the data while we are revalidating it.
 *
 * In this case we for example can still show a list in the tool window, but
 * disable the refresh button to prevent too many refresh requests.
 */
class RevalidatingState<T>(val result: T) : IntrospectionState<T>(true)

/**
 * Whenever something wrong happens, this state is emitted.
 */
class ErrorState<T>(
    val message: String,
    val cause: Throwable?,
) : IntrospectionState<T>(false)

typealias IntrospectionSubject<T> = Observable<IntrospectionState<T>>
typealias IntrospectionSubjectSource<T> = BehaviorSubject<IntrospectionState<T>>

class IntrospectionLifecycle<T>(private val state: IntrospectionSubjectSource<T>) {
    /**
     * This contains the state of the last introspection result.
     */
    private var lastIntrospectionResult: T? = null

    fun onLoadingStarted() {
        val lastResult = lastIntrospectionResult

        if (lastResult !== null) {
            state.onNext(RevalidatingState(lastResult))
        } else {
            state.onNext(InitialLoadingState())
        }
    }

    fun onError(message: String, cause: Throwable?) {
        lastIntrospectionResult = null
        state.onNext(ErrorState(message, cause))
    }

    fun onData(result: T) {
        lastIntrospectionResult = result
        state.onNext(LoadedState(result))
    }
}
