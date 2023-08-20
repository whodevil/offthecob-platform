package info.offthecob.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Unroll


class SpotlessCustomizationTest extends IntegrationSpec {

    @Unroll
    def "#checkTask finds styles that need to be applied"() {
        given:
        def gradleArguments = [
                checkTask,
                "-Pproject_directory=${projectDirectory}" as String,
                "--write-locks"
        ] as List<String>

        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(gradleArguments)
                .buildAndFail()

        then:
        result.task(":${checkTask}").getOutcome() == TaskOutcome.FAILED

        where:
        checkTask             | _
        "spotlessJavaCheck"   | _
        "spotlessKotlinCheck" | _
    }


    @Unroll
    def "#spotlessApply fixes #spotlessCheck"() {
        given:
        def gradleArguments = [
                spotlessApply,
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
        result.task(":${spotlessApply}").getOutcome() == TaskOutcome.SUCCESS

        and:
        def checkGradleArguments = [
                "clean",
                spotlessCheck,
                "-Pproject_directory=${projectDirectory}" as String,
                "--write-locks"
        ] as List<String>
        def checkResult = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(checkGradleArguments)
                .build()

        then:
        checkResult.task(":${spotlessCheck}").getOutcome() == TaskOutcome.SUCCESS

        where:
        spotlessApply         | spotlessCheck
        "spotlessKotlinApply" | "spotlessKotlinCheck"
        "spotlessJavaApply"   | "spotlessJavaCheck"
    }
}
