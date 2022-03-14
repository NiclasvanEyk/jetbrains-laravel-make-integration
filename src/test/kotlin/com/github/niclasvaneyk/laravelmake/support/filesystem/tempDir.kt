package com.github.niclasvaneyk.laravelmake.support.filesystem

import java.nio.file.Paths

fun temporaryDirectory(vararg paths: String) = Paths.get(System.getProperty("java.io.tmpdir"), *paths).toFile()
