package com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.notifications

import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.AutoconfigureLaravelSailAction
import com.github.niclasvaneyk.laravelmake.plugin.laravel.sail.SailNotificationGroup
import com.intellij.openapi.project.Project

fun offerSailAutoconfiguration(project: Project) = SailNotificationGroup
    .info(
        title = "Laravel Sail Setup",
        content = """
            It seems like this project uses the default Laravel Sail setup.
            To get a better development experience, the IDE can be 
            automatically configured to use the PHP and NPM installations from
            the Sail container.
        """.trimIndent()
    )
    .apply {
        isImportant = true
        addAction(AutoconfigureLaravelSailAction(hideIcon = true))
    }
    .notify(project)