plugins {
    kotlin("jvm") version "2.1.0"
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.3"
}

group = "nailheadbot"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    // logging
    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)

    // jda
    implementation(libs.net.dv8tion.jda)

    // coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.core.jvm)

    // exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    // database drivers
    implementation(libs.sqlite.jdbc)

    // lavaplayer
    implementation(libs.lavaplayer)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("bot.jar")
        manifest {
            attributes["Main-Class"] = "nailheadbot.NailheadBot"
        }
    }
}
