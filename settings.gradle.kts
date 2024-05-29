
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfig",
    "CruxMenu",
    "CruxPotion",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

