package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.migrations

import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class MigrationsFileChangeListener(
    private val service: MigrationService,
): AsyncFileListener {
    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        for (event in events) {
            if (event.path.matches(Regex(".*/migrations/.*\\.php"))) {
                service.refresh()
                break;
            }
        }

        return null
    }
}
