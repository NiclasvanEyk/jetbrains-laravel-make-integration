package com.github.niclasvaneyk.laravelmake.common.system

fun env(name: String, default: String? = null): String {
    return System.getenv(name) ?: default ?: ""
}
