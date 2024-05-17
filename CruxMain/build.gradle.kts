
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.crunch)
    implementation("org.jetbrains:annotations:24.1.0")
}

tasks{
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
