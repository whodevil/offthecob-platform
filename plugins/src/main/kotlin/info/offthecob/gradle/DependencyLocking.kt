package info.offthecob.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolutionStrategy
import org.gradle.api.artifacts.dsl.LockMode

const val RUNTIME_CLASSPATH = "runtimeClasspath"
const val COMPILE_CLASSPATH = "compileClasspath"
const val ANNOTATION_PROCESSOR = "annotationProcessor"
val LOCKED_CONFIGURATIONS = listOf(RUNTIME_CLASSPATH, COMPILE_CLASSPATH, ANNOTATION_PROCESSOR)

const val RESOLVE_AND_LOCK_ALL = "resolveAndLockAll"

class DependencyLocking : Plugin<Project> {
    override fun apply(project: Project) {
        enableLocking(project)
        lockAndResolveAll(project)
    }

    private fun lockAndResolveAll(project: Project) {
        project.tasks.register(RESOLVE_AND_LOCK_ALL) {
            doFirst {
                if (!project.gradle.startParameter.isWriteDependencyLocks) {
                    throw GradleException("--write-locks is not set")
                }
            }
            doLast {
                LOCKED_CONFIGURATIONS.forEach {
                    val configuration = project.configurations.getByName(it)
                    if (configuration.isCanBeResolved) {
                        configuration.resolve()
                    }
                }
            }
        }
    }

    private fun enableLocking(project: Project) {
        project.dependencyLocking.lockMode.set(LockMode.STRICT)
        project.configurations.apply {
            LOCKED_CONFIGURATIONS.forEach {
                getByName(it).resolutionStrategy(ResolutionStrategy::activateDependencyLocking)
            }
        }
    }
}
