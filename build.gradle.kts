//import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.4.21"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "0.4.0"
    // detekt linter - read more: https://detekt.github.io/detekt/kotlindsl.html
    // id("io.gitlab.arturbosch.detekt") version "1.17.1"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginName: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project

val phpPluginVersion: String by project

group = pluginGroup
version = pluginVersion

// Configure project's dependencies
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
//    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.test {
    useJUnitPlatform()
}

val ideVersion = "213.6777.58"
// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName = pluginName
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    alternativeIdePath = "/Users/niclasvaneyk/Library/Application Support/"
        .plus("JetBrains/Toolbox/apps/PhpStorm/ch-0/")
        .plus("$ideVersion/PhpStorm/Contents")

//  Plugin Dependencies:
//  https://www.jetbrains.org/intellij/sdk/docs/basics/plugin_structure/plugin_dependencies.html

    setPlugins("com.jetbrains.php:$phpPluginVersion")
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
//detekt {
//    config = files("./detekt-config.yml")
//    buildUponDefaultConfig = true
//    autoCorrect = true
//
//    reports {
//        html.enabled = false
//        xml.enabled = false
//        txt.enabled = false
//    }
//}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("0.40.0")
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    listOf("compileKotlin", "compileTestKotlin").forEach {
        getByName<KotlinCompile>(it) {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

//    withType<Detekt> {
//        jvmTarget = "1.8"
//    }

    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceBuild)
        untilBuild(null)

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
//        pluginDescription(closure {
//            File("./README.md").readText().lines().run {
//                subList(indexOf("<!-- Plugin description -->") + 1, indexOf("<!-- Plugin description end -->"))
//            }.joinToString("\n").run { markdownToHTML(this) }
//        })

        // Get the latest available change notes from the changelog file
        changeNotes(
            closure {
                changelog.getLatest().toHTML()
            }
        )
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        channels(
            pluginVersion
                .split('-')
                .getOrElse(1) { "default" }
                .split('.')
                .first()
        )
    }
}
