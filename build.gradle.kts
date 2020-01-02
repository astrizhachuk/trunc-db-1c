import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig
import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.CommitVersionDescription
import me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription

plugins {
    java
    id("io.franzbecker.gradle-lombok") version "3.2.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("me.qoomon.git-versioning") version "2.1.0"
}

repositories {
    mavenCentral()
}

group = "ru.astrizhachuk"

gitVersioning.apply(closureOf<GitVersioningPluginConfig> {
    preferTags = true
    branchVersionDescription(closureOf<VersionDescription> {
        pattern = "^(?!v[0-9]+).*"
        versionFormat = "\${branch}-\${commit.short}\${dirty}"
    })
    tag(closureOf<VersionDescription> {
        pattern = "v(?<tagVersion>[0-9].*)"
        versionFormat = "\${tagVersion}\${dirty}"
    })
    commit(closureOf<CommitVersionDescription> {
        versionFormat = "\${commit.short}\${dirty}"
    })
})

val junitVersion = "5.5.2"
val jacksonVersion = "2.10.0"

dependencies {

    implementation("commons-cli", "commons-cli", "1.4")
    implementation("commons-io", "commons-io", "2.6")

    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", jacksonVersion)
    implementation("com.fasterxml.jackson.dataformat", "jackson-dataformat-xml", jacksonVersion)

    implementation("org.slf4j", "slf4j-api", "1.8.0-beta4")
    implementation("org.slf4j", "slf4j-simple", "1.8.0-beta4")

    //   implementation("org.apache.httpcomponents", "httpclient","4.5.10")
    implementation("com.squareup.okhttp3", "okhttp", "4.3.0")

    compileOnly("org.projectlombok", "lombok", lombok.version)

    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
    testImplementation("org.assertj", "assertj-core", "3.13.2")
    testImplementation("com.ginsberg", "junit5-system-exit", "1.0.0")
    testImplementation("com.squareup.okhttp3", "mockwebserver", "4.3.0")

}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.astrizhachuk.Main"
        attributes["Implementation-ShadowJarVersion"] = archiveVersion.get()
    }

    enabled = false
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = listOf(project.configurations["implementation"])
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }

    reports {
        html.isEnabled = true
    }
}

lombok {
    version = "1.18.10"
}