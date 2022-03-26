package com.github.niclasvaneyk.laravelmake.common.collection

val Map<String, Any>.humanReadableKeys: String get() {
    return keys.joinToString(", ") { "'$it'" }
}
