package info.offthecob.gradle

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpotlessCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.getByType(SpotlessExtension::class.java).apply {
            java {
                importOrder()
                removeUnusedImports()
                googleJavaFormat()
                target("src/**/*.java")
            }
            kotlin {
                ktlint()
                target("src/**/*.kt")
            }
        }
    }
}
