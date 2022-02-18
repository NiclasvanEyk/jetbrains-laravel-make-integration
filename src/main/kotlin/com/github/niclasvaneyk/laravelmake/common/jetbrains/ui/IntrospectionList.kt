package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.components.JBList
import com.intellij.util.containers.toArray
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.*
import javax.swing.DefaultListModel

abstract class IntrospectionList<T>(
    protected val modelUpdates: IntrospectionSubject<List<T>>
) : JBList<T>(emptyList()), CanBeReRendered {
    var updatingModel: List<T> = emptyList()
    val listElements: Sequence<T>
        get() = (model as DefaultListModel).elements().iterator().asSequence()

    companion object {
        protected val log = Logger.getInstance(IntrospectionList::class.java)
    }

    protected fun subscribeToModelUpdates() {
        modelUpdates.subscribe(
            { state ->
                when (state) {
                    is LoadedState -> onLoaded(state.result)
                    is RevalidatedState -> onRevalidated(state.result)
                    is RevalidatingState -> onRevalidating(state.result)
                    else -> onOtherUpdate(state)
                }
            },
            { onError(it) }
        )
    }

    protected open fun onLoaded(result: List<T>) {
        refreshModel(result)
    }

    protected open fun onRevalidating(result: List<T>) {
        // No action required
    }

    private fun onRevalidated(result: List<T>) {
        refreshModel(result)
    }

    protected open fun onOtherUpdate(state: IntrospectionState<List<T>>) {
        refreshModel(emptyList())
    }

    protected open fun onError(error: Throwable) {
        log.error(error)
    }

    /**
     * Can be used to further transform / filter the new model values
     */
    protected open fun deriveVisibleModel(newModel: List<T>): List<T> {
        return newModel
    }

    /**
     * Helps to identify the elements after an update, so that we persist the
     * selection state across updates.
     */
    abstract fun listKey(element: T): String

    protected open fun refreshModel(newModel: List<T>) {
        ApplicationManager.getApplication().invokeLater {
            updatingModel = newModel
            val previouslySelected = selectedValue
            val previousKey = if (previouslySelected == null) null else listKey(previouslySelected)

            (model as DefaultListModel<T>).apply {
                removeAllElements()
                val visibleModel = deriveVisibleModel(newModel)

                addAll(visibleModel)
            }

            if (previousKey != null) {
                val indexOfKey = listElements.indexOfFirst { listKey(it) == previousKey }
                if (indexOfKey == -1) return@invokeLater
                selectedIndex = indexOfKey
            }
        }
    }

    override fun triggerRender() {
        refreshModel(updatingModel)
    }
}
