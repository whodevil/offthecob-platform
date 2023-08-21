package info.offthecob.gradle

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.springframework.boot.gradle.plugin.SpringBootPlugin

class SpringService : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply {
            apply(ServicePlugin::class.java)
            apply(DependencyManagementPlugin::class.java)
            apply(DependencyManagementCustomization::class.java)
            apply(SpringBootPlugin::class.java)
            apply(JibSpringCustomization::class.java)
        }
    }
}
