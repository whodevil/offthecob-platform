package info.offthecob.gradle

import org.intellij.lang.annotations.Language

class IntegrationTestUtils {
    private final File projectDir

    IntegrationTestUtils(File projectDir) {
        this.projectDir = projectDir
    }

    void setupDefaultProject() {
        defaultSettings()
        defaultBuildGradle()
        defaultJavaMain()
        defaultKotlin()
        defaultTesting()
    }

    void defaultSettings() {
        def settings = new File(projectDir, "settings.gradle.kts")
        def libraryDir = new File(System.getProperty("user.dir")).parentFile
        settings << """
        plugins {
            id("org.gradle.toolchains.foojay-resolver") version "0.6.0"
        }
        rootProject.name = "test-project"
        toolchainManagement {
            jvm {
                javaRepositories {
                    repository("foojay") {
                        resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
                    }
                }
            }
        }
        dependencyResolutionManagement {
            repositories {
                mavenCentral()
                gradlePluginPortal()
            }
            versionCatalogs {
                create("libs") {
                    from(files("${libraryDir.absolutePath}/gradle/libs.versions.toml"))
                }
            }
        }
        """.stripIndent()
    }

    void defaultBuildGradle() {
        def buildGradle = new File(projectDir, "build.gradle.kts")
        buildGradle << """
        plugins {
            id("info.offthecob.Base")
            kotlin("jvm") version "1.9.0"
        }
        dependencies {
            implementation(libs.google.guice)
            testImplementation(libs.spock.core)
            testImplementation(libs.junit)
            testRuntimeOnly(libs.junit.platform.launcher)
        }
        """.stripIndent()
    }

    void defaultJavaMain() {
        def srcMain = new File(projectDir, "src/main/java/info/offthecob/testing")
        srcMain.mkdirs()
        def mainFile = new File(srcMain, "Main.java")
        @Language("java")
        def main = """
        package info.offthecob.testing;
        public class Main {
            public static final String FORMATTED_STRING = "%s+1";
            
            public static void main(String[] args){
                System.out.println("boom");
            }
            
            public String method(String input) {
                return FORMATTED_STRING.formatted(input);
            }
        }
        """.stripIndent()
        mainFile << main
    }

    void defaultTesting() {
        def srcTest = new File(projectDir, "src/test/groovy/info/offthecob/testing")
        srcTest.mkdirs()
        def mainTestFile = new File(srcTest, "MainTest.groovy")
        @Language("groovy")
        def mainTest = """
        package info.offthecob.testing
        import spock.lang.Specification
        class MainTest extends Specification {
            def "main outputs expected value"() {
                given:
                def input = "test input"
                
                when:
                def observed = new Main().method(input)
                
                then:
                observed == Main.FORMATTED_STRING.formatted(input)
            }
        }
        """.stripIndent()
        mainTestFile << mainTest
    }

    void defaultKotlin() {
        def srcMain = new File(projectDir, "src/main/kotlin/info/offthecob/testing")
        srcMain.mkdirs()
        def kotlinFile = new File(srcMain, "KotlinFunctional.kt")

        // new line below is intentional because it causes the spotless linter to fail
        def function = """
        package info.offthecob.testing
        fun functionalFun(input: String, input2: String,
                          input3: String): String { return "${'$'}input functional" }
        """.stripIndent()
        kotlinFile << function
    }
}
