package com.github.niclasvaneyk.laravelmake.common.filesystem

/**
 * Loads the contents of a resource from the `resources` directory.
 */
fun resource(path: String): String {
    return object{}.javaClass.getResource(path).readText()
}