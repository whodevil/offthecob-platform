plugins {
    `java-platform`
}

dependencies {
    constraints {
        api(libs.bundles.spock)
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("gpr") {
            from(components["javaPlatform"])
        }
    }
}
