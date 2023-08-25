package info.offthecob.gradle

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir

class IntegrationSpec extends Specification {
    @Shared
    @TempDir
    File projectDirectory

    @Shared
    IntegrationTestUtils utils

    def setupSpec() {
        utils = new IntegrationTestUtils(projectDirectory)
        utils.setupDefaultProject()
    }
}
