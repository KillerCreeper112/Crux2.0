
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

    "CruxTest"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

