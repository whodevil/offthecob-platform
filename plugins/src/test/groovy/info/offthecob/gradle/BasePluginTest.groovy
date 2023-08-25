package info.offthecob.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class BasePluginTest extends IntegrationSpec {
    def "happy path"() {
        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(utils.happyPathGradleArgs())
                .build()

        then:
        result.task(":check").getOutcome() == TaskOutcome.SUCCESS
    }
}
