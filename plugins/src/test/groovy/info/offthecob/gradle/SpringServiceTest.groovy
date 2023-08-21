package info.offthecob.gradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir

class SpringServiceTest extends Specification {
    @Shared
    @TempDir
    File projectDirectory

    def setupSpec() {
        new IntegrationTestUtils(projectDirectory).springProject()
    }

    def "happy path"() {
        given:
        def gradleArguments = [
                "clean",
                "spotlessApply",
                "check",
                "jibBuildTar",
                "-Pproject_directory=${projectDirectory}" as String,
                "--write-locks"
        ] as List<String>

        when:
        def result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(gradleArguments)
                .build()

        then:
        result.task(":jibBuildTar").outcome == TaskOutcome.SUCCESS
    }
}
