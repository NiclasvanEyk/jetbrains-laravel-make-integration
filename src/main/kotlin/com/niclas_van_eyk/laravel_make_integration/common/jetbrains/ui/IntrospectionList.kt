package com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui

import com.intellij.ui.components.JBList
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.IntrospectionState
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.IntrospectionSubject
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.LoadedState
import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.RevalidatingState
import javax.swing.DefaultListModel

abstract class IntrospectionList<T>(
    protected val modelUpdates: IntrospectionSubject<List<T>>
) : JBList<T>(emptyList()), CanBeReRendered {
    var updatingModel: List<T> = emptyList()

    protected fun subscribeToModelUpdates() {
        modelUpdates.subscribe { state ->
            when (state) {
                is LoadedState -> onLoaded(state.result)
                is RevalidatingState -> onRevalidating(state.result)
                else -> onOtherUpdate(state)
            }
        }
    }

    protected open fun onLoaded(result: List<T>) {
        refreshModel(result)
    }

    protected open fun onRevalidating(result: List<T>) {
        refreshModel(result)
    }

    protected open fun onOtherUpdate(state: IntrospectionState<List<T>>) {
        refreshModel(emptyList())
    }

    /**
     * Can be used to further transform / filter the new model values
     */
    protected open fun deriveVisibleModel(newModel: List<T>): List<T> {
        return newModel
    }

    private fun refreshModel(newModel: List<T>) {
        updatingModel = newModel
        (model as DefaultListModel).apply {
            removeAllElements()
            val visibleModel = deriveVisibleModel(newModel)

            addAll(visibleModel)
        }
    }

    override fun triggerRender() {
        refreshModel(updatingModel)
    }
}
