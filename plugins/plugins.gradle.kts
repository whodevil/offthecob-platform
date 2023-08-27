plugins {
    `java-gradle-plugin`
    java
    groovy
    `kotlin-dsl`
    alias(libs.plugins.spotless)
    alias(libs.plugins.gradle.publish.plugin)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

group = "info.offthecob"
version = System.getenv("REVISION") ?: "SNAPSHOT"

gradlePlugin {
    website.set("https://github.com/whodevil/offthecob-platform")
    vcsUrl.set("https://github.com/whodevil/offthecob-platform.git")
    plugins {
        create("base") {
            id = "info.offthecob.Base"
            implementationClass = "info.offthecob.gradle.BasePlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions"))
            displayName = "Offthecob Base"
            description =
                """
                This plugin contains a bunch of best practices around how to build projects for the JVM.
                This includes dependency locking by default, kotlin support, groovy support (for testing), 
                wiring up the junit platform, null away, and linting.
                
                The Base plugin also offers a task to help update lock files:
                ./gradlew resolveAndLockAll --write-locks
                """.trimIndent()

        }
        create("service") {
            id = "info.offthecob.Service"
            implementationClass = "info.offthecob.gradle.ServicePlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "jib", "containers"))
            displayName = "Offthecob JVM Service"
            description =
                """
                This plugin contains all the work from 'info.offthecob.Base' but includes 'jib' for building
                containers.    
                """.trimIndent()
        }
        create("library") {
            id = "info.offthecob.Library"
            implementationClass = "info.offthecob.gradle.LibraryPlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "library"))
            displayName = "Offthecob JVM Library"
            description =
                """
                This plugin contains all the work from 'info.offthecob.Base' but includes 'java-library' for
                building jvm libraries.
                """.trimIndent()
        }
        create("springService") {
            id = "info.offthecob.SpringService"
            implementationClass = "info.offthecob.gradle.SpringService"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "jib", "containers", "spring"))
            displayName = "Offthecob JVM Spring Service"
            description =
                """
                This plugin contains all the work from 'info.offthecob.Service' but includes customization of
                the 'dependency-management' plugin used by spring, and applies the 'kotlin-spring' and 
                'spring-boot' plugins.
                """.trimIndent()
        }
        create("settings") {
            id = "info.offthecob.Settings"
            implementationClass = "info.offthecob.gradle.SettingsPlugin"
            tags.set(listOf("conventions", "settings"))
            displayName = "Offthecob Settings"
            description =
                """
                * Includes the foojay plugin for gradle 9 support of the toolchain api. 
                * Auto imports sub projects.
                
                This also changes gradle to expect sub project's build.gradle.kts to be 
                <sub project name>.gradle.kts. For example:
                
                * build.gradle.kts
                * settings.gradle.kts
                * my-service/my-service.gradle.kts
                * my-library/my-library.gradle.kts
                """.trimIndent()
        }
    }
}

dependencies {
    implementation(gradleApi())

    api(libs.foojay.resolver)
    api(libs.jib)
    api(libs.kotlin.jvm.plugin)
    api(libs.kotlin.allopen)
    api(libs.nullaway.plugin)
    api(libs.spotless)
    api(libs.spring.boot.gradle)
    api(libs.spring.boot.jib.extension)
    api(libs.spring.dependency.management)

    compileOnly(libs.jetbrains.annotations)

    testImplementation(gradleTestKit())
    testImplementation(libs.spock.core)
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    named("testClasses") {
        dependsOn("pluginUnderTestMetadata")
    }
    named("publishPlugins") {
        dependsOn("handlePluginPublishSecrets")
    }
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
    register("handlePluginPublishSecrets") {
        doLast {
            val key = System.getenv("GRADLE_PUBLISH_KEY")
                ?: throw GradleException("Attempting to publish plugin without GRADLE_PUBLISH_KEY set.")
            val secret = System.getenv("GRADLE_PUBLISH_SECRET")
                ?: throw GradleException("Attempting to publish plugin without GRADLE_PUBLISH_SECRET set.")
            System.setProperty("gradle.publish.key", key)
            System.setProperty("gradle.publish.secret", secret)
        }
    }
}

spotless {
    kotlin {
        ktlint()
        target("src/**/*.kt")
    }
}
