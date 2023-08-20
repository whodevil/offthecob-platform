plugins {
    id("org.gradle.toolchains.foojay-resolver") version "0.6.0"
}

toolchainManagement {
    jvm {
        javaRepositories {
            repository("foojay") {
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}

rootProject.name = "jvm-platform"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.projectDir.listFiles().filter { it.isDirectory }.map { subDir ->
    subDir.listFiles().filter {
        it.isFile && it.name.contains(".gradle.kts")
    }.map {
        File(it.parent).name
    }
}.flatten().forEach {
    include(it)
}

rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle.kts"
    assert(project.buildFile.isFile)
}
