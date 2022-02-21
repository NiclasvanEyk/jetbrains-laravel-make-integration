package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

/**
 * Refreshes the contents of the Laravel tool windows routes tab, if any file
 * in `routes/` change.
 */
class RoutesFileChangeListener(
    private val routes: RouteIntrospecter,
): AsyncFileListener {
    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier? {
        for (event in events) {
            if (event.path.matches(Regex(".*/routes/.*\\.php"))) {
                routes.refresh()
                break;
            }
        }

        return null
    }
}