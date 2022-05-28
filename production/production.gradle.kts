plugins {
    `java-platform`
}

dependencies {
    constraints {
        api(libs.bundles.logging)
        api(libs.bundles.guice)
        api(libs.guava)
        api(libs.bundles.apache.commons)
    }
}
