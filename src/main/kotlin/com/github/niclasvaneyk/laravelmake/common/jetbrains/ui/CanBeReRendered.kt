package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import kotlin.reflect.KProperty

interface CanBeReRendered {
    fun triggerRender()
}

class TriggersRender<T>(
    initial: T,
    private val renderTarget: CanBeReRendered,
) {
    private var delegationTarget: T = initial

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegationTarget

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegationTarget = value
        renderTarget.triggerRender()
    }
}
