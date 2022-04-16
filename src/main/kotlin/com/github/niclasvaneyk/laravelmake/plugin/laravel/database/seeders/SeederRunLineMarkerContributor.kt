package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.seeders

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement

class SeederRunLineMarkerContributor: RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        val `class` = element.runnableSeederClass ?: return null

        return Info(
            AllIcons.RunConfigurations.TestState.Run,
            // For some reason the entries are in here twice,
            // so we only want run, debug and edit run config
            ExecutorAction.getActions(),
            { `class`.name }
        )
    }
}