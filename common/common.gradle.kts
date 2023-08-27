plugins {
    `java-library`
    signing
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
                url.set("https://github.com/whodevil/offthecob-platform")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/license/mit/")
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
                    connection.set("scm:git:git://github.com/whodevil/offthecob-platform.git")
                    developerConnection.set("scm:git:ssh://github.com:whodevil/offthecob-platform.git")
                    url.set("https://github.com/whodevil/offthecob-platform/tree/master")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["Common"])
}
