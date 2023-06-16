plugins {
    kotlin("jvm") version "1.5.30"
    id("java")
    id("java-library")
    id("application")
    id("idea")
    id("maven-publish")
}

// Required for Gradle 7.0 and JitPack
publishing.publications.create<MavenPublication>("maven").from(components["java"])

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

idea.module.isDownloadSources = true
idea.module.isDownloadJavadoc = true

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

val gradleDependencyVersion = "7.2"

tasks.wrapper {
    gradleVersion = gradleDependencyVersion
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
    // Required for Gradle Tooling API
    maven(url = "https://repo.gradle.org/gradle/libs-releases-local/")
}

java {
    withSourcesJar()
    withJavadocJar()
}

// Specify all of our dependency versions
val awsSdk2Version = "2.20.86"
val jodaTimeVersion = "2.10.10"
val immutablesValueVersion = "2.8.9-ea-1"

// NOTE: Do not use 1.2.3 or you will get null pointer exceptions
val pahoVersion = "1.2.5"

val junitVersion = "4.13.2"
val guavaVersion = "30.1.1-jre"
val vavrVersion = "0.10.4"

group = "local"
version = "1.0-SNAPSHOT"

dependencies {
    // Immutables (requires annotation processing for code generation)
    annotationProcessor("org.immutables:value:$immutablesValueVersion")
    annotationProcessor("org.immutables:gson:$immutablesValueVersion")
    implementation("org.immutables:value:$immutablesValueVersion")
    implementation("org.immutables:gson:$immutablesValueVersion")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:$pahoVersion")
    implementation("joda-time:joda-time:$jodaTimeVersion")

    implementation("software.amazon.awssdk:iot:$awsSdk2Version")
    implementation("software.amazon.awssdk:sts:$awsSdk2Version")
    implementation("software.amazon.awssdk:apache-client:$awsSdk2Version")

    implementation("io.vavr:vavr:$vavrVersion")

    api("javax.inject:javax.inject:1")

    testImplementation("junit:junit:$junitVersion")
    testImplementation("com.google.guava:guava:$guavaVersion")
}
