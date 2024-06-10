
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
    "CruxBlocks",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

