package info.offthecob.gradle

import com.google.cloud.tools.jib.gradle.JibExtension

class JibSpringCustomizationTest extends UnitSpec {
    def setupSpec() {
        setupProject("info.offthecob.SpringService")
    }

    def "Spring extension is applied"() {
        when:
        def implementation = project.extensions.getByType(JibExtension.class).getPluginExtensions().get().first().getImplementation()

        then:
        implementation == JibSpringCustomizationKt.JIB_SPRING_BOOT_EXTENSION
    }
}
