package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.php

/**
 * Extracts he basename out of the fully qualified class name.
 *
 * Example: `MyNamespace\Http\Controllers\MyController` -> `MyController`
 */
fun basename(fqn: String) = fqn.substringAfterLast("\\")
