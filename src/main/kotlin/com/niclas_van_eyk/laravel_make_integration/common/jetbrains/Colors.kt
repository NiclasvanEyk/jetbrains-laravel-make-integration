package com.niclas_van_eyk.laravel_make_integration.common.jetbrains

import com.intellij.openapi.project.Project
import com.intellij.ui.FileColorManager
import org.jetbrains.annotations.Nullable
import java.awt.Color

class Colors {
    companion object {
        fun vendor(project: Project): @Nullable Color? {
            // It seems that the tree view uses this color to indicate that
            // a file belongs to a library, so we will do the same
            return FileColorManager.getInstance(project).getColor("Yellow")
        }
    }
}
