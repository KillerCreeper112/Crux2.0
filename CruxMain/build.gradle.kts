
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.crunch)
    implementation("com.ezylang:EvalEx:3.2.0")

    compileOnly(files(
        "E:\\Plugins\\FIVERR\\A_ICEYPLAYZ\\cclaim\\run\\plugins\\PlaceholderAPI-2.11.4.jar"
    ))
}

tasks{
    assemble{
        dependsOn(shadowJar)
    }
    shadowJar{
        archiveClassifier = null
    }
    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}
