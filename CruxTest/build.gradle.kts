
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(project(":CruxMain"))
    implementation(project(":CruxConfigs"))
    implementation(project(":CruxPotions"))
    implementation(project(":CruxMenus"))
    implementation(project(":CruxEntities"))
    implementation(project(":CruxAttributes"))
    implementation(project(":CruxEnchants"))
    implementation(project(":CruxItems"))
    implementation(project(":CruxBlocks"))
    implementation(project(":CruxStructures"))
    implementation(project(":CruxExternal"))
    implementation(project(":CruxAdvancements"))
}

tasks{
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
