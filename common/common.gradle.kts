plugins {
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("Common") {
            from(components["java"])
        }
    }
}
