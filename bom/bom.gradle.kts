plugins {
    id("offthecob-platform.maven-conventions")
    `java-platform`
}

dependencies {
    constraints {
        api(project(":common"))
        api(project(":catalog"))
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("BOM") {
            from(components["javaPlatform"])
            pom {
                name.set("bom")
                description.set("Bill of Materials (BOM) for info.offthecob.platform")
            }
        }
    }
}

signing {
    sign(publishing.publications["BOM"])
}
