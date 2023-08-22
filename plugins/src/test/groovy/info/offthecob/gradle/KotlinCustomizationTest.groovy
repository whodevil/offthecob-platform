package info.offthecob.gradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

import static info.offthecob.gradle.DefaultVersionsKt.KOTLIN_DEFAULT_VERSION


class KotlinCustomizationTest extends IntegrationSpec {

    def "kotlin version can be overridden"() {
        given:
        def settingsGradle = new File(projectDirectory, "settings.gradle.kts")
        settingsGradle.delete()
        def settings = """
        plugins {
            id("org.gradle.toolchains.foojay-resolver") version "0.6.0"
        }
        rootProject.name = "test-project"
        toolchainManagement {
            jvm {
                javaRepositories {
                    repository("foojay") {
                        resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
                    }
                }
            }
        }
        dependencyResolutionManagement {
            repositories {
                mavenCentral()
                gradlePluginPortal()
            }
            versionCatalogs {
                create("libs"){
                    $versionString
                    library("google-guice", "com.google.inject:guice:5.1.0")
                    library("spock-core", "org.spockframework:spock-core:2.2-M1-groovy-3.0")
                    library("junit", "org.junit.jupiter:junit-jupiter-api:5.9.1")
                    library("junit-platform-launcher", "org.junit.platform:junit-platform-launcher:1.9.3")
                }
            }
        }
        """.stripIndent()
        settingsGradle << settings
        def gradleArguments = [
                "compileKotlin",
                "dependencies",
                "-x",
                "test",
                "-Pproject_directory=${projectDirectory}" as String,
                "--write-locks"
        ] as List<String>

        when:
        def result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(gradleArguments)
                .build()

        then:
        result.output.contains("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        where:
        kotlinVersion          | versionString
        "1.8.21"               | """version("kotlin", "$kotlinVersion")"""
        KOTLIN_DEFAULT_VERSION | ""
    }

}
