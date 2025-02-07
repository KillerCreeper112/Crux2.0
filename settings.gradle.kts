
rootProject.name = "Crux"

include(
    "CruxMain",
    "CruxConfigs",
    "CruxMenus",
    "CruxPotions",

    "CruxAttributes",
    "CruxStats",
    "CruxEnchantsLegacy",
    "CruxEnchants",
    "CruxForm",

    "CruxItems",
    "CruxBlocks",
    "CruxBrewery",

    "CruxAdvancements",
    "CruxStructures",

    "CruxExternal",

    "CruxGeneration",
    "CruxWorlds",

    "CruxEntities",
    "CruxStatistics",
    "CruxTickables",

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

