package info.offthecob.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class BasePluginTest extends IntegrationSpec {
    def "happy path"() {
        given:
        def gradleArguments = [
                "clean",
                "spotlessApply",
                "check",
                "-Pproject_directory=${projectDirectory}" as String,
                "--write-locks"
        ] as List<String>

        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(gradleArguments)
                .build()

        then:
        result.task(":check").getOutcome() == TaskOutcome.SUCCESS
    }
}
