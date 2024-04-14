pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "people"
include(":businessPeople")
include(":presentation")
include(":useCasePeople")
include(":persistence")
include(":quoteGarden")
include(":avatarsDicebear")