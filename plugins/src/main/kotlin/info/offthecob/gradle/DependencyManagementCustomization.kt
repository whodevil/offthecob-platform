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
        project
            .extensions
            .getByType(DependencyManagementExtension::class.java)
            .imports {
                listOf(
                    Pair(
                        "org.springframework.boot:spring-boot-dependencies",
                        getVersion(project, SPRING_BOOT, SPRING_BOOT_DEFAULT_VERSION),
                    ),
                    Pair(
                        "com.netflix.graphql.dgs:graphql-dgs-platform-dependencies",
                        getVersion(project, DGS, DGS_DEFAULT_VERSION),
                    ),
                )
                    .forEach { (path, version) ->
                        mavenBom("$path:$version")
                    }
            }
    }
}
