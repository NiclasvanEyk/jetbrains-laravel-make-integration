package com.github.niclasvaneyk.laravelmake.common.laravel.introspection

class CouldNotExtractJsonException(
    source: String,
    cause: Throwable
): RuntimeException(
    "Could not extract JSON while introspecting. Source:\n$source",
    cause,
)