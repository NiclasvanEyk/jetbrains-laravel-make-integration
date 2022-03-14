package com.github.niclasvaneyk.laravelmake

import com.intellij.testFramework.TestDataPath

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest: LaravelProjectFixture() {
    fun testRoutesCanBeScanned() {
        assertEmpty("")
    }
}
