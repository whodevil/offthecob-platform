JVM Platform
============
This is a set of "jvm platforms" intended to help me keep my personal projects
libraries up to date.

# Catalog
Produces an artifact containing the library versions maintained in `gradle/libs.versions.toml`.
This artifact can be used downstream in other projects with the gradle `versionsCatalog`
api. Essentially the catalog helps keep the list of approved libraries maintained in 
one place.

# Plugins
A list of best practices around dependency locking, linting, and other build best practices.
This subproject also allows me to do all the things I do in every project in one place, cleaning up
the downstream consumer's gradle files.

If a version catalog exists with the following version definitions, the plugins below will use those version
instead of the defaults.

* `kotlin` manages the version of the kotlin toolchain.
* `dgs` the version of Netflix DGS BOM to expose to the dependency management plugin.
* `spring-boot` the version of Spring Boot BOM to expose to the dependency management plugin.

## Settings
This plugin auto imports submodules, adds foojay for gradle 9 toolchain api support, and updates the submodule 
gradle file naming:

```shell
/build.gradle.kts
/settings.gradle.kts
/my-service/my-service.gradle.kts
/my-library/my-library.gradle.kts
```

Add the following to the top of the settings.gradle.kts file:
```gradle
plugins {
    id("info.offthecob.Settings")
}
```

## Base
This plugin contains what I think are best practices for all jvm projects.

```gradle
plugins {
    id("info.offthecob.Base")
}
```

## Library
This includes everything in base, but enables the `java-library` plugin, adding the api configuration
as well as the jar task

```gradle
plugins {
    id("info.offthecob.Library")
}
```

## Service
Similar to library, where it is based on the base plugin, but this plugin adds jib (a java native container builder).

```gradle
plugins {
    id("info.offthecob.Service")
}
```

## Spring Service
This uses everything in Service, but also adds the Spring special sauce.
Specifically, it adds the `spring-boot`, `dependency-management` and `kotlin-spring` plugins into scope,
and customizes them with best practices. It also applies the jib extension for Spring.

```gradle
plugins {
    id("info.offthecob.SpringService")
}
```
