package info.offthecob.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec

val JVM_VERSION = JavaLanguageVersion.of(17)
val JVM_VENDOR = JvmVendorSpec.ADOPTIUM

class JavaCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        val java = project.extensions.findByType(JavaPluginExtension::class.java)
        setupToolchain(java!!)
        project.tasks.withType(Test::class.java, Test::useJUnitPlatform)
    }

    private fun setupToolchain(java: JavaPluginExtension) {
        java.toolchain.apply {
            languageVersion.set(JVM_VERSION)
            vendor.set(JVM_VENDOR)
        }
    }
}
