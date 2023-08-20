package info.offthecob.gradle

import spock.lang.Shared
import spock.lang.TempDir

import static info.offthecob.gradle.DependencyLockingKt.RESOLVE_AND_LOCK_ALL

class DependencyLockingTest extends UnitSpec {
    @Shared
    @TempDir
    File projectDir

    def setupSpec() {
        setupProject("info.offthecob.Base")
    }

    def "locking helper task exists"() {
        given:
        def resolveAndLockAll = project.getTasksByName(RESOLVE_AND_LOCK_ALL, true)

        expect:
        !resolveAndLockAll.isEmpty()
    }
}
