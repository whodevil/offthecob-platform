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
        register<MavenPublication>("gpr") {
            from(components["versionCatalog"])
        }
    }
}
