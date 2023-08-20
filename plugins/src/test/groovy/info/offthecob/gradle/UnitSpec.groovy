package info.offthecob.gradle

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

import static info.offthecob.gradle.BasePluginKt.PROJECT_BUILDER_TEST

class UnitSpec extends Specification {
    @Shared
    Project project

    def setupProject(String pluginName) {
        project = ProjectBuilder.builder().build()
        def ext = project.extensions.getByType(ExtraPropertiesExtension.class)
        ext.set(PROJECT_BUILDER_TEST, "true")
        project.getPluginManager().apply(pluginName)
    }
}
