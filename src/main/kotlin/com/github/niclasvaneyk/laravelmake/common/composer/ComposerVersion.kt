package com.github.niclasvaneyk.laravelmake.common.composer

data class ComposerVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    constructor(value: String) : this(
        value.split(".").getOrNull(0)?.toInt() ?: 0,
        value.split(".").getOrNull(1)?.toInt() ?: 0,
        value.split(".").getOrNull(2)?.toInt() ?: 0,
    )

    operator fun compareTo(other: ComposerVersion): Int {
        if (major != other.major) return major - other.major
        if (minor != other.minor) return minor - other.minor
        if (patch != other.patch) return patch - other.patch

        return 0
    }
}
