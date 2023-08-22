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
version = System.getenv("REVISION") ?: "v9999999999"

gradlePlugin {
    website.set("https://github.com/whodevil/jvm-platform")
    vcsUrl.set("https://github.com/whodevil/jvm-platform.git")
    plugins {
        create("base") {
            id = "info.offthecob.Base"
            implementationClass = "info.offthecob.gradle.BasePlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions"))
            displayName = "Offthecob JVM Base"
            description = "This plugin contains a bunch of best practices around how to build projects for the JVM. " +
                    "This includes dependency locking by default, kotlin support, groovy support (for testing), wiring up the " +
                    "junit platform, null away, and linting."
        }
        create("service") {
            id = "info.offthecob.Service"
            implementationClass = "info.offthecob.gradle.ServicePlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "jib", "containers"))
            displayName = "Offthecob JVM Service"
            description =
                "This plugin contains all the work from `info.offthecob.Base` but includes `jib` for building " +
                        "containers."
        }
        create("library") {
            id = "info.offthecob.Library"
            implementationClass = "info.offthecob.gradle.LibraryPlugin"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "library"))
            displayName = "Offthecob JVM Library"
            description =
                "This plugin contains all the work from `info.offthecob.Base` but includes `java-library` for " +
                        "building jvm libraries."
        }
        create("springService") {
            id = "info.offthecob.SpringService"
            implementationClass = "info.offthecob.gradle.SpringService"
            tags.set(listOf("kotlin", "java", "groovy", "conventions", "jib", "containers", "spring"))
            displayName = "Offthecob JVM Spring Service"
            description =
                "This plugin contains all the work from `info.offthecob.Service` but includes customization of " +
                        "the dependency management plugin used by spring, and applies the `kotlin-spring` and `spring-boot` " +
                        "plugins."
        }
    }
}

dependencies {
    implementation(gradleApi())
    api(libs.jib)
    api(libs.nullaway.plugin)
    api(libs.spotless)
    api(libs.spring.boot.gradle)
    api(libs.spring.boot.jib.extension)
    api(libs.spring.dependency.management)
    api(libs.kotlin.jvm.plugin)
    api(libs.kotlin.allopen)
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
