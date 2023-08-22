package info.offthecob.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        configureKotlinPluginExtension(project)
        configureKotlinCompileTasks(project)
    }

    private fun configureKotlinPluginExtension(project: Project) {
        val kotlinExtension = project.extensions.findByType(KotlinProjectExtension::class.java)!!
        kotlinExtension.coreLibrariesVersion = getVersion(project, "kotlin", KOTLIN_DEFAULT_VERSION)
    }

    private fun configureKotlinCompileTasks(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            kotlinOptions {
                freeCompilerArgs += "-Xjsr305=strict"
                jvmTarget = "$JVM_DEFAULT_VERSION"
            }
        }
    }
}
