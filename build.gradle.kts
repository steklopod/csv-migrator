plugins {
    kotlin("jvm") version "1.5.30-RC"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}
repositories { mavenCentral() }

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")
    implementation("io.github.blackmo18:kotlin-grass-core-jvm:1.0.0")
    implementation("io.github.blackmo18:kotlin-grass-parser-jvm:0.8.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation ("io.github.microutils:kotlin-logging-jvm:2.0.10")
    implementation ("org.slf4j:slf4j-api:2.0.0-alpha4")
    implementation ("org.slf4j:slf4j-simple:2.0.0-alpha4")
}

application {
    val name = "csv.migrator.App"
    mainClass.set(name)
    // Required by ShadowJar.
    mainClassName = name
}
group = "csv.migrator"
tasks{ withType<Jar> { manifest { attributes["Main-Class"] = application.mainClass } } }
