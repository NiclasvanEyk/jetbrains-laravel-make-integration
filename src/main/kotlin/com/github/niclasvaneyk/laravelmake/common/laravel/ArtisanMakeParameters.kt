package com.github.niclasvaneyk.laravelmake.common.laravel

import kotlin.collections.ArrayList

data class ArtisanMakeParameters(val className: String, val parameters: MutableList<String>) {
    companion object {
        fun fromInput(input: String): ArtisanMakeParameters {
            val parts = input.split(' ').toMutableList()
            val name = parts[0].trim()
            val params =
                if (parts.size > 1) parts.subList(1, parts.size)
                else ArrayList()

            return ArtisanMakeParameters(name, params)
        }
    }

    fun asList(): List<String> {
        val arr = parameters.toMutableList()
        arr.add(0, className)

        return arr
    }

    fun humanReadable(subCommand: String): String {
        return "artisan make:$subCommand $className ${parameters.joinToString(" ")}"
    }
}
