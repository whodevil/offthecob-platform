package info.offthecob.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.jvm
import org.gradle.toolchains.foojay.FoojayToolchainResolver
import org.gradle.toolchains.foojay.FoojayToolchainsPlugin
import org.slf4j.LoggerFactory
import java.io.File

const val BUILD_SRC = "buildSrc"
const val PROCESSING_SUB_PROJECT_GRADLE = "Processing sub project gradle: %s"

class SettingsPlugin : Plugin<Any> {
    private val logger = LoggerFactory.getLogger(SettingsPlugin::class.java)

    override fun apply(target: Any) =
        if (target is Settings) {
            apply(target)
        } else {
            throw GradleException("Settings plugins must be applied in the settings script.")
        }

    fun apply(settings: Settings) {
        toolChainSupport(settings)
        includeSubProjects(settings)
        subProjectGradleNamingConvention(settings)
    }

    private fun subProjectGradleNamingConvention(settings: Settings) {
        settings.rootProject.children.forEach { project ->
            logger.info("Renaming project build file for project: ${project.name}")
            project.buildFileName = "${project.name}.gradle.kts"
            assert(project.buildFile.isFile)
        }
    }

    private fun includeSubProjects(settings: Settings) {
        settings.rootProject
            .projectDir
            .listFiles()
            ?.filter(subDirectoriesNotNamedBuildSrc())
            ?.map { subDir ->
                subDir
                    .listFiles()
                    ?.filter {
                        it.isFile && it.name.contains(".gradle.kts")
                    }
                    ?.map {
                        logger.info(String.format(PROCESSING_SUB_PROJECT_GRADLE, it.name))
                        File(it.parent).name
                    }
                    ?: throw GradleException("There was a problem importing the sub projects")
            }
            ?.flatten()
            ?.forEach {
                settings.include(it)
            }
    }

    private fun subDirectoriesNotNamedBuildSrc(): (File) -> Boolean = { it.isDirectory and !it.name.contains(BUILD_SRC) }

    private fun toolChainSupport(settings: Settings) {
        settings.plugins.apply(FoojayToolchainsPlugin::class.java)
        settings.toolchainManagement.apply {
            jvm {
                javaRepositories {
                    repository("foojay") {
                        resolverClass.set(FoojayToolchainResolver::class.java)
                    }
                }
            }
        }
    }
}
