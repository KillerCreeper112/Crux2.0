
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.crunch)

    compileOnly(files(
        "E:\\Plugins\\FIVERR\\A_ICEYPLAYZ\\cclaim\\run\\plugins\\PlaceholderAPI-2.11.4.jar"
    ))
}

tasks{
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
