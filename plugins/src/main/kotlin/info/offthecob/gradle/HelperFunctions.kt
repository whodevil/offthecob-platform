package info.offthecob.gradle

import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtraPropertiesExtension

fun pluginUnderTest(project: Project): Boolean {
    val ext = project.extensions.getByType(ExtraPropertiesExtension::class.java)
    return ext.properties.containsKey(PROJECT_BUILDER_TEST)
}

fun getVersion(
    project: Project,
    name: String,
    default: String,
): String {
    val catalog: VersionCatalog? =
        try {
            project.rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        } catch (_: UnknownDomainObjectException) {
            null
        }

    return if (catalog != null && catalog.findVersion(name).isPresent) {
        val version = catalog.findVersion(name).get().toString()
        project.logger.info("Using catalog version $version for $name")
        version
    } else {
        project.logger.info(
            "Plugin looked in Version Catalog 'libs' for version $name, and did not find anything, using default value $default.",
        )
        default
    }
}
