package info.offthecob.gradle

import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

private const val SPRING_BOOT = "spring.boot"
private const val DGS = "dgs"

class DependencyManagementCustomization : Plugin<Project> {
    override fun apply(project: Project) {
        if (pluginUnderTest(project)) {
            project.logger.log(LogLevel.WARN, "plugin under test, bypassing DependencyManagementCustomization")
            return
        }
        val dependencyManagement = project.extensions.getByType(DependencyManagementExtension::class.java)
        val catalog = versionCatalog(project)
        dependencyManagement.imports {
            listOf(
                Pair(
                    "org.springframework.boot:spring-boot-dependencies",
                    if (catalog.findVersion(SPRING_BOOT).isPresent) catalog.findVersion(SPRING_BOOT).get() else "3.1.2",
                ),
                Pair(
                    "com.netflix.graphql.dgs:graphql-dgs-platform-dependencies",
                    if (catalog.findVersion(DGS).isPresent) catalog.findVersion(DGS).get() else "7.3.6",
                ),
            )
                .forEach { (path, version) ->
                    mavenBom("$path:$version")
                }
        }
    }
}
