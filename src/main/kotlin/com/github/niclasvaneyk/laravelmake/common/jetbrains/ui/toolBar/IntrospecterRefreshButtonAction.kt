package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui.toolBar

import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter

class IntrospecterRefreshButtonAction<T>(
    introspecter: CommandBasedIntrospecter<T>,
    description: String,
    text: String = "Refresh",
) : RefreshButtonAction(
    text,
    description,
    isRefreshing = introspecter.introspectionState.map { it.loading },
    refresh = { introspecter.refresh() }
) {
}
