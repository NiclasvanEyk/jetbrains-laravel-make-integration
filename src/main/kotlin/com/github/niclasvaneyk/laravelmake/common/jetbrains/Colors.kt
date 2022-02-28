package com.github.niclasvaneyk.laravelmake.common.jetbrains

import com.intellij.openapi.project.Project
import com.intellij.ui.FileColorManager
import org.jetbrains.annotations.Nullable
import java.awt.Color

class Colors {
    companion object {
        fun vendor(project: Project): Color? {
            // It seems that the tree view uses this color to indicate that
            // a file belongs to a library, so we will do the same
            return FileColorManager.getInstance(project).getColor("Yellow")
        }
    }
}
