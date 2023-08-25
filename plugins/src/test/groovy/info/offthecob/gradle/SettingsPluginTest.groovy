package info.offthecob.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir

import static info.offthecob.gradle.SettingsPluginKt.BUILD_SRC


class SettingsPluginTest extends Specification {
    @Shared
    @TempDir
    File projectDirectory

    @Shared
    IntegrationTestUtils utils

    def setupSpec() {
        utils = new IntegrationTestUtils(projectDirectory)
        utils.defaultSettings()
        ["project-a", "project-b", BUILD_SRC].forEach {
            def subProjectDirectory = new File(projectDirectory, it)
            subProjectDirectory.mkdirs()
            def subProjectGradle = new File(subProjectDirectory, "${it}.gradle.kts")
            utils.buildGradle(subProjectGradle)
            utils.mainJava(subProjectDirectory)
            utils.testing(subProjectDirectory)
            utils.kotlin(subProjectDirectory)
        }
    }

    def "happy path"() {
        given:
        def args = utils.happyPathGradleArgs()
        args.add("--info")

        when:
        BuildResult result = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(projectDirectory)
                .withArguments(args)
                .build()

        then: "expected sub projects are successful"
        result.task(":project-a:check").getOutcome() == TaskOutcome.SUCCESS
        result.output.contains(SettingsPluginKt.PROCESSING_SUB_PROJECT_GRADLE.formatted("project-a.gradle.kts"))
        result.task(":project-b:check").getOutcome() == TaskOutcome.SUCCESS
        result.output.contains(SettingsPluginKt.PROCESSING_SUB_PROJECT_GRADLE.formatted("project-b.gradle.kts"))

        and: "buildSrc is ignored by the Settings plugin"
        !result.output.contains(SettingsPluginKt.PROCESSING_SUB_PROJECT_GRADLE.formatted("${BUILD_SRC}.gradle.kts"))
    }
}
