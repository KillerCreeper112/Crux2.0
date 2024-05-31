
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(project(":CruxMain"))
    implementation(project(":CruxConfig"))
    implementation(project(":CruxPotion"))
    implementation("com.ezylang:EvalEx:3.2.0")
}

tasks{
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
