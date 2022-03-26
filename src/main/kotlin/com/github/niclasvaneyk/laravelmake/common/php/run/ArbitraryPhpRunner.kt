package com.github.niclasvaneyk.laravelmake.common.php.run

import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan

class ArbitraryPhpRunner(private val artisan: Artisan) {
    fun run(php: String): PHPRunner.Result {
        return artisan.command("tinker", parameters = listOf("--execute", php))
    }
}
