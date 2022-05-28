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
            from(files("libs.versions.toml"))
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