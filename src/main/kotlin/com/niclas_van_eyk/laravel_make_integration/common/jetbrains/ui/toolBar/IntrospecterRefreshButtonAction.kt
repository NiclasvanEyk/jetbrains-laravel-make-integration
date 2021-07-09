package com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.toolBar

import com.niclas_van_eyk.laravel_make_integration.common.laravel.introspection.CommandBasedIntrospecter

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
