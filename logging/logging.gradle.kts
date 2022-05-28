plugins {
    `java-library`
    `groovy`
}

dependencies {
    api(libs.bundles.logging)
    implementation(libs.gson)
    testImplementation(libs.bundles.spock)
    testImplementation(libs.bundles.groovy)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
