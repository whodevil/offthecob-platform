package info.offthecob.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtraPropertiesExtension

fun pluginUnderTest(project: Project): Boolean {
    val ext = project.extensions.getByType(ExtraPropertiesExtension::class.java)
    return ext.properties.containsKey(PROJECT_BUILDER_TEST)
}

fun versionCatalog(project: Project): VersionCatalog {
    try {
        return project.rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
    } catch (_: UnknownDomainObjectException) {
        throw GradleException("This plugin expects a Version Catalog named 'libs', see https://docs.gradle.org/current/userguide/platforms.html")
    }
}
