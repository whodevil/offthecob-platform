package info.offthecob.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin

class LibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply {
            apply(BasePlugin::class.java)
            apply(JavaLibraryPlugin::class.java)
        }
    }
}
