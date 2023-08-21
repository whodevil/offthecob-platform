plugins {
    `java-gradle-plugin`
    java
    groovy
    `kotlin-dsl`
    alias(libs.plugins.spotless)
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

gradlePlugin {
    plugins {
        create("base") {
            id = "info.offthecob.Base"
            implementationClass = "info.offthecob.gradle.BasePlugin"
        }
        create("service") {
            id = "info.offthecob.Service"
            implementationClass = "info.offthecob.gradle.ServicePlugin"
        }
        create("library") {
            id = "info.offthecob.Library"
            implementationClass = "info.offthecob.gradle.LibraryPlugin"
        }
        create("springService") {
            id = "info.offthecob.SpringService"
            implementationClass = "info.offthecob.gradle.SpringService"
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

    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

spotless {
    kotlin {
        ktlint()
        target("src/**/*.kt")
    }
}
