
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfigs",
    "CruxMenus",
    "CruxPotions",

    "CruxAttributes",
    "CruxEnchants",
    "CruxEntities",

    "CruxItems",
    "CruxBlocks",
    "CruxBrewery",

    "CruxAdvancements",
    "CruxStructures",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

