package com.github.niclasvaneyk.laravelmake.plugin.laravel.database.seeders

import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.extends
import com.github.niclasvaneyk.laravelmake.common.jetbrains.php.psi.isPhpIdentifier
import com.github.niclasvaneyk.laravelmake.plugin.laravel.laravel
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass

val PsiElement.runnableSeederClass: PhpClass?
    get() {
        if (!isPhpIdentifier()) return null

        val `class` = parent
        if (`class` !is PhpClass) return null
        if (`class`.isAbstract) return null
        if (!`class`.extends("\\Illuminate\\Database\\Seeder")) return null

        project.laravel ?: return null

        return `class`
    }
