package com.niclas_van_eyk.laravel_make_integration.laravel

data class ComposerVersion(
        val major: Int,
        val minor: Int,
        val patch: Int
) {
    constructor(value: String): this(
            value.split(".").getOrNull(0)?.toInt() ?: 0,
            value.split(".").getOrNull(1)?.toInt() ?: 0,
            value.split(".").getOrNull(2)?.toInt() ?: 0
    )

    constructor(): this(0, 0, 0)

    fun greaterOrEqualThan(other: ComposerVersion): Boolean {
        if (major > other.major) return true
        if (major < other.major) return false

        // Now the majors are equal
        if (minor > other.minor) return true
        if (minor < other.minor) return false

        // Now the majors and minors are equal
        if (patch > other.patch) return true
        if (patch < other.patch) return true

        // Here everything is the same
        return true
    }
}