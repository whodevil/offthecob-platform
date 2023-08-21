package info.offthecob.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class DependencyLockingIntegrationTest extends IntegrationSpec {
    def "verify locking is enabled"() {
        given:
        def gradleArguments = [
                "clean",
                "spotlessApply",
                "check",
                "--write-locks",
                "-Pproject_directory=${projectDirectory}" as String
        ] as List<String>

        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(gradleArguments)
                .build()

        then:
        result.task(":compileKotlin").getOutcome() == TaskOutcome.SUCCESS
    }
}
