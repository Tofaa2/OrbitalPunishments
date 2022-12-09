import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "net.orbitalstudios"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {

    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    implementation("mysql:mysql-connector-java:8.0.28")
    implementation(kotlin("stdlib-jdk8"))

}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("OrbitalPunishments.jar")
        doLast {
            copy {
                val file = file("${project.buildDir}/libs/OrbitalPunishments.jar")
                val pluginsDir = file("${project.rootDir}/server/plugins")
                if (!pluginsDir.exists()) {
                    pluginsDir.mkdirs()
                }
                file.copyTo(pluginsDir.resolve(file.name), overwrite = true)

                // For testing
                val config = file("${project.rootDir}/server/plugins/OrbitalPunishments/config.yml")
                if (config.exists()) {
                    config.delete()
                }
            }
        }
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}