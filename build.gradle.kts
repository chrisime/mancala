import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.springframework.boot") version "2.2.6.RELEASE"

    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.71"

    //id("nu.studer.jooq") version "4.1" apply false
    //id("org.flywaydb.flyway") version "6.3.2" apply false
}

repositories {
    mavenCentral()
}

description = "Mancala Game"
group = "com.bol"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xstrict-java-nullability-assertions",
                "-Xuse-experimental=kotlin.Experimental"
            )
            apiVersion = "1.3"
            languageVersion = "1.3"
            jvmTarget = "11"
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xstrict-java-nullability-assertions",
                "-Xuse-experimental=kotlin.Experimental"
            )
            apiVersion = "1.3"
            languageVersion = "1.3"
            jvmTarget = "11"
            javaParameters = true
        }
    }

    test {
        useJUnitPlatform()
        failFast = true

        testLogging {
            lifecycle {
                events = setOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                showStandardStreams = true
            }

            info.events = lifecycle.events
            info.exceptionFormat = lifecycle.exceptionFormat
        }

    }

}

configure<DependencyManagementExtension> {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }

    overriddenByDependencies(false)
}

configurations {
    compile {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
        exclude(group = "junit", module = "junit")
    }
}

val developmentOnly: Configuration by configurations.register("developmentOnly")

configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }

    all {
        exclude(module = "slf4j-log4j12")
        exclude(module = "log4j")
        exclude(module = "log4j-api")
        exclude(module = "log4j-to-slf4j")
        exclude(module = "undertow-websockets-jsr")
        exclude(module = "junit-vintage-engine")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    //    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //    api("org.springframework.boot:spring-boot-starter-jdbc")
    //    implementation("com.zaxxer:HikariCP")
    //    implementation("org.postgresql:postgresql")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("io.github.microutils:kotlin-logging:1.7.+")

    testApi("org.springframework.boot:spring-boot-starter-test")
//    testApi("org.springframework.security:spring-security-test")
}

springBoot {
    mainClassName = "com.bol.mancala.starter.MancalaStarterKt"
}

tasks {

    bootRun {
        main = "com.bol.mancala.starter.MancalaStarterKt"
        sourceResources(sourceSets["main"])
        systemProperties = System.getProperties().mapKeys { it.key as String }
        classpath = sourceSets["main"].runtimeClasspath + configurations["developmentOnly"]
    }

    bootJar {
        enabled = true
        isExcludeDevtools = true
        mainClassName = "com.bol.mancala.starter.MancalaStarterKt"
        archiveClassifier.set("boot")
    }

}

