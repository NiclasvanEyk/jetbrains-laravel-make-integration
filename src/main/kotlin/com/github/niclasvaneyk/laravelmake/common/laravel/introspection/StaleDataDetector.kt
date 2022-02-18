package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

import java.time.Duration
import java.time.LocalDateTime

/**
 * Can detect whether or not some piece of data is considered stale and should
 * be refreshed.
 *
 * Intended to be used in conjunction with a [CommandBasedIntrospecter] to know
 * whether to refresh its data or not.
 */
class StaleDataDetector(
    /**
     * After this duration the data is being seen as stale and should be
     * refreshed.
     */
    private val refreshAfter: Duration = Duration.ofSeconds(30)
) {
    private var lastRefreshedAt: LocalDateTime = LocalDateTime.now()

    /**
     * From this point in time the data is being considered stale and should be
     * refreshed.
     * @see refreshAfter
     */
    private val refreshBoundary: LocalDateTime
        get() = lastRefreshedAt.plus(refreshAfter)

    /**
     * Whether the data is considered stale or not.
     */
    val isStale: Boolean
        get() = LocalDateTime.now().isAfter(refreshBoundary)

    fun markAsRefreshed() {
        lastRefreshedAt = LocalDateTime.now()
    }
}
