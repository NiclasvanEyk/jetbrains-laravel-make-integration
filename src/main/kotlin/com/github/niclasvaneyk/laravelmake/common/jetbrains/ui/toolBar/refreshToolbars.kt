package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.intellij.ide.ActivityTracker

/**
 * See https://plugins.jetbrains.com/docs/intellij/basic-action-system.html
 *
 * This refreshes toolbar icons, so when e.g. the routes get updated, but the
 * user does not explicitly focus the routes tool window, the refresh
 * button will still update its "disabled" status correctly. Otherwise, updates
 * like this only happen, once the user focuses a given pane or with a
 * noticeable delay.
 */
fun refreshToolbars() = ActivityTracker.getInstance().inc()
