plugins {
    `version-catalog`
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("Catalog") {
            from(components["versionCatalog"])
            pom {
                name.set("Catalog")
                description.set("Gradle version catalog supporting info.offthecob.platform")
                url.set("https://github.com/whodevil/jvm-platform")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://raw.githubusercontent.com/whodevil/jvm-platform/main/LICENSE")
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
                    developerConnection.set("scm:git:https://github.com/whodevil/jvm-platform.git")
                    url.set("https://github.com/whodevil/jvm-platform")
                }
            }
        }
    }
}
