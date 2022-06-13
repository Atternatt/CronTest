import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

group = "com.m2f"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xcontext-receivers")
        }
    }

    test {
        useJUnit()
    }
}

application {
    mainClass.set("MainKt")
}