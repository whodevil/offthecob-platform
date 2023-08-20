package info.offthecob.gradle


class ServicePluginTest extends UnitSpec {
    def setupSpec() {
        setupProject("info.offthecob.Service")
    }

    def "contains jib task"() {
        given:
        def jib = project.getTasksByName("jib", true)

        expect:
        !jib.isEmpty()
    }
}
