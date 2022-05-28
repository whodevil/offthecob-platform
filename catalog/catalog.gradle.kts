plugins {
    `version-catalog`
}

catalog {
    versionCatalog {
        from(files("../libs.versions.toml"))
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("gpr") {
            from(components["versionCatalog"])
        }
    }
}
