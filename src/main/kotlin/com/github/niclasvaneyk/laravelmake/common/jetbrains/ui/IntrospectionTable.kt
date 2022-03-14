package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.components.JBList
import com.intellij.util.containers.toArray
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.*
import com.intellij.ui.table.JBTable
import javax.swing.DefaultListModel
import javax.swing.table.DefaultTableModel

abstract class IntrospectionTable<T>(
    model: KJBTableModel<T>,
    protected val modelUpdates: IntrospectionSubject<List<T>>
) : KJBTable<T>(model), CanBeReRendered {
    companion object {
        protected val log = Logger.getInstance(IntrospectionTable::class.java)
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
//            val previouslySelected = selectedValue
//            val previousKey = if (previouslySelected == null) null else listKey(previouslySelected)

            val visibleModel = deriveVisibleModel(newModel)
            kjbModel.refresh(visibleModel)

//            if (previousKey != null) {
//                val indexOfKey = listElements.indexOfFirst { listKey(it) == previousKey }
//                if (indexOfKey == -1) return@invokeLater
//                selectedIndex = indexOfKey
//            }
        }
    }

    override fun triggerRender() {
        refreshModel(kjbModel.data())
    }
}
