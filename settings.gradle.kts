
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfig",
    "CruxMenu",
    "CruxPotions",

    "CruxAttributes",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

