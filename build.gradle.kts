plugins {
    alias(libs.plugins.paperweight) apply false
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow) apply false
}
subprojects{
    plugins.apply("java")

    repositories {
        mavenCentral()
        maven("https://redempt.dev")
    }
}