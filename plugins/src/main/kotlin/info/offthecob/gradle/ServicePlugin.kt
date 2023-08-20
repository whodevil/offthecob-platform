package info.offthecob.gradle

import com.google.cloud.tools.jib.gradle.JibPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class ServicePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply {
            apply(BasePlugin::class.java)
            apply(JibPlugin::class.java)
        }
    }
}
