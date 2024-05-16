
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfig",
    "CruxMenu"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

