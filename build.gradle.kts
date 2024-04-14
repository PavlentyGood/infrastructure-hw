import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.9.23"
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion apply false
	id("com.palantir.docker") version "0.36.0"
	id("com.palantir.docker-compose") version "0.36.0"
	id("io.gitlab.arturbosch.detekt") version "1.23.6"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.owasp.dependencycheck") version "9.1.0"
	id("net.researchgate.release") version "3.0.2"
	jacoco
}

docker {
	name = project.name
	copySpec.from("build/libs").into("build/libs")
}

detekt {
	buildUponDefaultConfig = true
}

dependencyCheck {
	analyzers.assemblyEnabled = false
}

allprojects {
	group = "com.stringconcat"
	version = "0.0.1-SNAPSHOT"

	apply {
		plugin("io.gitlab.arturbosch.detekt")
	}

	repositories {
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

}

subprojects {
	apply {
		plugin("io.gitlab.arturbosch.detekt")
	}

	detekt {
		buildUponDefaultConfig = true
	}
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
	// spring modules
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")

	// kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation(project(":presentation"))
	implementation(project(":persistence"))
	implementation(project(":useCasePeople"))
	implementation(project(":businessPeople"))
	implementation(project(":quoteGarden"))
	implementation(project(":avatarsDicebear"))

	// dev tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	//persistance
	implementation("org.postgresql:postgresql:42.7.3")
	implementation("org.liquibase:liquibase-core:4.27.0")

	// tests
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
}

tasks.bootJar {
	archiveFileName = "${project.name}.jar"
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.17".toBigDecimal()
			}
		}
	}
}

tasks.dockerPrepare {
	dependsOn(tasks.build)
}

tasks.dockerComposeUp {
	dependsOn(tasks.docker)
}

tasks.check {
	finalizedBy(tasks.dependencyUpdates)
}

tasks.dependencyUpdates {
	revision = "release"

	fun isNonStable(version: String): Boolean {
		val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
		val regex = "^[0-9,.v-]+(-r)?$".toRegex()
		val isStable = stableKeyword || regex.matches(version)
		return isStable.not()
	}

	rejectVersionIf {
		isNonStable(candidate.version)
	}
}
