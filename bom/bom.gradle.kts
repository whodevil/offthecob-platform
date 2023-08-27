plugins {
    `java-platform`
    signing
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
                url.set("https://github.com/whodevil/jvm-platform")
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
                    connection.set("scm:git:git://github.com/whodevil/jvm-platform.git")
                    developerConnection.set("scm:git:ssh://github.com:whodevil/jvm-platform.git")
                    url.set("https://github.com/whodevil/jvm-platform/tree/master")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["BOM"])
}
