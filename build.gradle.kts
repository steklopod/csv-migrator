plugins {
    application
    kotlin("jvm") version "1.5.30-RC"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.github.ben-manes.versions") version "0.39.0"
}
repositories { mavenCentral() }

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")
    implementation("io.github.blackmo18:kotlin-grass-core-jvm:1.0.0")
    implementation("io.github.blackmo18:kotlin-grass-parser-jvm:0.8.0")
    val slf4 = "2.0.0-alpha4"
    implementation ("org.slf4j:slf4j-api:$slf4")
    implementation ("org.slf4j:slf4j-simple:$slf4")
}
group = "csv.migrator"
application { val name = "$group.AppKt"; mainClass.set(name); mainClassName = name }
