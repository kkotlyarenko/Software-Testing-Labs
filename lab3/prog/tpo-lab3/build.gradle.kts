plugins {
    kotlin("jvm") version "2.3.20"
}

group = "org.kkotlyarenko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.22.0")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.9.2")
}

kotlin {
    jvmToolchain(25)
}

fun Test.commonTestConfig() {
    // Wire custom Test tasks to the test source set (built-in `test` is wired already).
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform()
    maxParallelForks = 1
    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
    systemProperties(System.getProperties().filterKeys { it is String } as Map<String, Any>)

    val browser = (systemProperties["browser"] ?: "chrome").toString()

    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    // Per-test line + final summary, prefixed with the browser so parallel runs stay readable.
    beforeTest(closureOf<org.gradle.api.tasks.testing.TestDescriptor> {
        logger.lifecycle("[$browser] ▶ ${className?.substringAfterLast('.')}.$name")
    })
    afterTest(KotlinClosure2<org.gradle.api.tasks.testing.TestDescriptor, org.gradle.api.tasks.testing.TestResult, Unit>({ descriptor, result ->
        val mark = when (result.resultType) {
            org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS -> "✅ PASSED"
            org.gradle.api.tasks.testing.TestResult.ResultType.FAILURE -> "❌ FAILED"
            org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED -> "⏭ SKIPPED"
        }
        val seconds = "%.1f".format((result.endTime - result.startTime) / 1000.0)
        logger.lifecycle("[$browser] $mark ${descriptor.className?.substringAfterLast('.')}.${descriptor.name} (${seconds}s)")
    }))
    afterSuite(KotlinClosure2<org.gradle.api.tasks.testing.TestDescriptor, org.gradle.api.tasks.testing.TestResult, Unit>({ descriptor, result ->
        if (descriptor.parent == null) {
            logger.lifecycle(
                "[$browser] ──────── Итог: всего ${result.testCount} | " +
                    "✅ ${result.successfulTestCount} | ❌ ${result.failedTestCount} | ⏭ ${result.skippedTestCount} ────────"
            )
        }
    }))
}

tasks.test {
    commonTestConfig()
}

tasks.register<Test>("testChrome") {
    group = "verification"
    description = "Run tests in Chrome"
    commonTestConfig()
    systemProperty("browser", "chrome")
}

tasks.register<Test>("testFirefox") {
    group = "verification"
    description = "Run tests in Firefox"
    commonTestConfig()
    systemProperty("browser", "firefox")
}
