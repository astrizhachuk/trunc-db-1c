plugins {
    java
    id("io.franzbecker.gradle-lombok") version "3.2.0"
}

group = "ru.astrizhachuk"
// TODO: 24.12.2019  auto.sem.ver
version = "0.1"

repositories {
    mavenCentral()
}

val junitVersion = "5.5.2"

dependencies {

    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    implementation("commons-cli", "commons-cli", "1.4")

    implementation("org.slf4j", "slf4j-api", "1.8.0-beta4")
    implementation("org.slf4j", "slf4j-simple", "1.8.0-beta4")

    compileOnly("org.projectlombok", "lombok", lombok.version)

    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
    testImplementation("org.assertj", "assertj-core", "3.13.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.astrizhachuk.Main"
    }
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