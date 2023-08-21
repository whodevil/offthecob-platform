package info.offthecob.gradle

import com.google.cloud.tools.jib.gradle.JibExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

const val JIB_SPRING_BOOT_EXTENSION = "com.google.cloud.tools.jib.gradle.extension.springboot.JibSpringBootExtension"

class JibSpringCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.getByType(JibExtension::class.java).pluginExtensions {
            pluginExtension {
                implementation = JIB_SPRING_BOOT_EXTENSION
            }
        }
    }
}
