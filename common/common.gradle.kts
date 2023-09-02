plugins {
    id("offthecob-platform.maven-conventions")
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("Common") {
            from(components["java"])
            pom {
                name.set("Common")
                description.set("Common library supporting info.offthecob.platform")
            }
        }
    }
}

signing {
    sign(publishing.publications["Common"])
}
