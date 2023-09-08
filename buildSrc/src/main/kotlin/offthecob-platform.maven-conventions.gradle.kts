plugins {
    signing
    `maven-publish`
}

group = "info.offthecob.platform"
version = System.getenv("REVISION") ?: "SNAPSHOT"

configure<PublishingExtension> {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/whodevil/offthecob-platform")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}
