package com.github.niclasvaneyk.laravelmake.common.filesystem

import com.intellij.openapi.vfs.VirtualFile
import java.util.LinkedList

fun VirtualFile.hasParents(vararg parents: String): Boolean {
    val queue = LinkedList(parents.toList())
    var currentRealParent = parent
    while (queue.isNotEmpty()) {
        val currentExpectedParent = queue.pollLast()
        if (currentRealParent?.name != currentExpectedParent) return false

        currentRealParent = currentRealParent?.parent
    }

    return queue.isEmpty()
}