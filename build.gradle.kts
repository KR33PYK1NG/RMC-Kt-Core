val rmcRepoUser: String by project
val rmcRepoPass: String by project

val rmcGroup = "rmc.kt.plugins"
val rmcArtifact = "core"
val rmcVersion = "1.2.0"
val rmcBaseName = "RMC-Kt-Core"

group = rmcGroup
version = rmcVersion

@Suppress("DEPRECATION")
base.archivesBaseName = rmcBaseName

plugins {
    `maven-publish`
    kotlin("jvm") version "1.6.0"
}

java {
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<ProcessResources> {
    expand("rmcBaseName" to rmcBaseName,
           "rmcVersion" to rmcVersion)
}

tasks.create<Copy>("dumpDeps") {
    from(configurations.runtimeClasspath)
    into("build/deps/")
}

configure<PublishingExtension> {
    repositories {
        maven {
            setUrl("https://repo.rus-minecraft.ru/maven/")
            credentials {
                username = rmcRepoUser
                password = rmcRepoPass
            }
        }
    }
    publications {
        create<MavenPublication>("release") {
            artifactId = rmcArtifact
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    }
}

dependencies {
    add("implementation", "com.zaxxer:HikariCP:5.0.0")
    add("compileOnly", "org.apache.logging.log4j:log4j-api:2.14.1")
    add("compileOnly", "org.spigotmc:spigot-api:1.18-rc3-R0.1-SNAPSHOT")
}
