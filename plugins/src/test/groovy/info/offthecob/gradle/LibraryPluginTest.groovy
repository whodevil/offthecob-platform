package info.offthecob.gradle

import spock.lang.Specification

class LibraryPluginTest extends UnitSpec {
    def setupSpec() {
        setupProject("info.offthecob.Library")
    }

    def "contains jib task"() {
        given:
        def jar = project.getTasksByName("jar", true)

        expect:
        !jar.isEmpty()
    }
}
