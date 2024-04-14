plugins {
    kotlin("jvm")
    id("net.researchgate.release")
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":businessPeople"))

    implementation("javax.inject:javax.inject:1")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}