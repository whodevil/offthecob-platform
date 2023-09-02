import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.registering


class PomPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("maven-publish")
        project.extensions.getByType(PublishingExtension::class.java).publications.withType(MavenPublication::class.java) {
            pom {
                url.set("https://github.com/whodevil/offthecob-platform")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }
                developers {
                    developer {
                        id.set("whodevil")
                        name.set("Devon Gleeson")
                        email.set("whodevil@offthecob.info")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/whodevil/offthecob-platform.git")
                    developerConnection.set("scm:git:ssh://github.com:whodevil/offthecob-platform.git")
                    url.set("https://github.com/whodevil/offthecob-platform/tree/master")
                }
            }
        }
    }
}
