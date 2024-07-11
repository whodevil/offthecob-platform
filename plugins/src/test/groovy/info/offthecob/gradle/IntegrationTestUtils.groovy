package info.offthecob.gradle

import org.intellij.lang.annotations.Language

class IntegrationTestUtils {
    private final File projectDir

    IntegrationTestUtils(File projectDir) {
        this.projectDir = projectDir
    }

    def happyPathGradleArgs() {
        return [
                "clean",
                "spotlessApply",
                "check",
                "-Pproject_directory=${projectDir}" as String,
                "--write-locks"
        ] as List<String>
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
            id("info.offthecob.Settings")
        }
        rootProject.name = "test-project"
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
        def buildGradleFile = new File(projectDir, "build.gradle.kts")
        buildGradle(buildGradleFile)
    }

    void buildGradle(File buildGradleFile) {
        buildGradleFile << """
        plugins {
            id("info.offthecob.Base")
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
        mainJava(projectDir)
    }

    void mainJava(File parentDir) {
        def srcMain = new File(parentDir, "src/main/java/info/offthecob/testing")
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
        testing(projectDir)
    }

    void testing(File parentDir) {
        def srcTest = new File(parentDir, "src/test/groovy/info/offthecob/testing")
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
            
            def "kotlin expected outputs"() {
                given:
                def input = "input"
                def input2 = "input2"
                def input3 = "input3"
                
                when:
                def observed = KotlinFunctionalKt.functionalFun(input, input2, input3)
                
                then:
                observed == "\$input \$input2 \$input3 functional"
            }
        }
        """.stripIndent()
        mainTestFile << mainTest
    }

    void defaultKotlin() {
        kotlin(projectDir)
    }

    void kotlin(File parentDir) {
        def srcMain = new File(parentDir, "src/main/kotlin/info/offthecob/testing")
        srcMain.mkdirs()
        def kotlinFile = new File(srcMain, "KotlinFunctional.kt")

        // new line below is intentional because it causes the spotless linter to fail
        def function = """
        package info.offthecob.testing
        fun functionalFun(input: String, input2: String,
                          input3: String): String { return "${'$'}input ${'$'}input2 ${'$'}input3 functional" }
        """.stripIndent()
        kotlinFile << function
    }

    void springProject() {
        defaultSettings()
        springBuildGradle()
        defaultJavaMain()
    }

    void springBuildGradle() {
        def buildGradle = new File(projectDir, "build.gradle.kts")
        buildGradle << """
        plugins {
            id("info.offthecob.SpringService")
        }
        dependencies {
            implementation("org.springframework.boot:spring-boot-starter")
            implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
            implementation("com.azure:azure-identity")            
        }
        """.stripIndent()
    }
}
