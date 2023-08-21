package info.offthecob.gradle

import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test

class JavaCustomizationTest extends UnitSpec {

    def setupSpec() {
        setupProject("info.offthecob.Base")
    }

    def "toolchain configuration"() {
        when:
        def toolchain = project.getExtensions().getByType(JavaPluginExtension.class).getToolchain()

        then:
        toolchain.languageVersion.get() == JavaCustomizationKt.JVM_VERSION
        toolchain.vendor.get() == JavaCustomizationKt.JVM_VENDOR
    }

    def "test use junit platform"() {
        given:
        def tests = project.tasks.withType(Test.class)

        expect:
        tests.each {
            if(!(it.testFramework instanceof JUnitPlatformTestFramework)) {
                assert false
            }
        }
    }
}
