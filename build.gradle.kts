import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.6.0"
	id("org.springframework.boot") version "2.2.1.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion apply false
	id("com.palantir.docker") version "0.36.0"
	id("com.palantir.docker-compose") version "0.36.0"
	id("io.gitlab.arturbosch.detekt") version "1.11.0"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("org.owasp.dependencycheck") version "8.2.1"
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

	repositories {
		mavenCentral()
		jcenter()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "1.8"
		}
	}

}

java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly: Configuration by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}



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
	implementation("org.liquibase:liquibase-core:4.9.1")

	// tests
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
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
				minimum = "0.2".toBigDecimal()
			}
		}
	}
}

tasks.docker.configure {
	dependsOn(tasks.bootJar)
}

tasks.check {
	finalizedBy(tasks.dependencyUpdates)
}

tasks.dependencyUpdates {
	revision = "release"

	fun isNonStable(version: String): Boolean {
		val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
		val regex = "^[0-9,.v-]+(-r)?$".toRegex()
		val isStable = stableKeyword || regex.matches(version)
		return isStable.not()
	}

	rejectVersionIf {
		isNonStable(candidate.version)
	}
}
