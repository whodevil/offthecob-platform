plugins {
    id("offthecob-platform.maven-conventions")
    `version-catalog`
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
        val bom = System.getenv("REVISION") ?: "SNAPSHOT"
        library("offthecob-platform", "info.offthecob.platform:bom:$bom")
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("Catalog") {
            from(components["versionCatalog"])
            pom {
                name.set("Catalog")
                description.set("Gradle version catalog supporting info.offthecob.platform")
            }
        }
    }
}

signing {
    sign(publishing.publications["Catalog"])
}
