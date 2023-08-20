package info.offthecob.gradle

import net.ltgt.gradle.nullaway.NullAwayExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

const val NULL_AWAY_ANNOTATED_PACKAGES = "net.ltgt"

class NullAwayCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.getByType(NullAwayExtension::class.java).apply {
            annotatedPackages.add(NULL_AWAY_ANNOTATED_PACKAGES)
        }
    }
}
