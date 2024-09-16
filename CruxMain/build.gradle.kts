
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    implementation(libs.crunch)
    implementation("com.ezylang:EvalEx:3.3.0")
}

tasks{

    assemble{
        dependsOn(shadowJar)
    }
    shadowJar{
        archiveClassifier = null
        relocate("redempt", "killercreepr.crux.redempt")
    }
}
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}
tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}
