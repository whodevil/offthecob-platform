plugins {
    id("org.gradle.toolchains.foojay-resolver") version "0.7.0"
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

rootProject.name = "offthecob-platform"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.projectDir.listFiles()
    .filter { it.isDirectory && !it.name.contains("buildSrc") }
    .map { subDir ->
        subDir.listFiles()
            .filter { it.isFile && it.name.contains(".gradle.kts") }
            .map { File(it.parent).name }
    }
    .flatten()
    .forEach { include(it) }

rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle.kts"
    assert(project.buildFile.isFile)
}
