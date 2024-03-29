package info.offthecob.gradle

import com.diffplug.gradle.spotless.SpotlessPlugin
import net.ltgt.gradle.nullaway.NullAwayPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin

const val PROJECT_BUILDER_TEST = "project-builder-test"

class BasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply {
            apply(JavaPlugin::class.java)
            apply(JavaCustomization::class.java)
            apply(GroovyPlugin::class.java)
            apply("kotlin")
            apply("kotlin-allopen")
            apply(KotlinCustomization::class.java)

            apply(NullAwayPlugin::class.java)
            apply(NullAwayCustomization::class.java)
            apply(DependencyLocking::class.java)

            // Spotless is not currently compatible with the ProjectBuilder test framework
            if (!pluginUnderTest(project)) {
                apply(SpotlessPlugin::class.java)
                apply(SpotlessCustomization::class.java)
            }
        }
    }
}
