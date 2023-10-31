plugins {
    kotlin("jvm") version "1.9.0"
}

group = "pw.mihou"
version = "1.0.0"
description = "Entour is a set of handy built-in components, hooks and tools that can be used with Nexus.R"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("pw.mihou:Nexus:3c2cda5d73")
    implementation("org.javacord:javacord:3.8.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}