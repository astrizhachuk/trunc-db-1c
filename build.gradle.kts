plugins {
    java
}

group = "ru.astrizhachuk"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    compile("commons-cli:commons-cli:1.4")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}