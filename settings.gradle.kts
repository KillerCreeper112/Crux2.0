
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfigs",
    "CruxMenu",
    "CruxPotions",

    "CruxAttributes",
    "CruxEnchants",
    "CruxEntities",

    "CruxItems",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

