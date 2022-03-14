package com.github.niclasvaneyk.laravelmake

import com.github.niclasvaneyk.laravelmake.support.LaravelProjectDescriptor
import com.intellij.testFramework.LightPlatformCodeInsightTestCase
import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class LaravelProjectFixture: LightPlatformCodeInsightTestCase() {
    override fun getProjectDescriptor() = LaravelProjectDescriptor()
}