plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
    id("net.researchgate.release")
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":businessPeople"))
    implementation(project(":useCasePeople"))

    // spring modules
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.18")
    implementation("org.springframework.boot:spring-boot-starter-data-rest:2.7.18")

    // tools
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    // view
    implementation( "org.jetbrains.kotlinx:kotlinx-html-jvm:0.11.0")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.18") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test:3.6.5")
}