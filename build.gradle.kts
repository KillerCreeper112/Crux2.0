plugins {
    alias(libs.plugins.paperweight) apply false
    alias(libs.plugins.runPaper)
}
subprojects{

    plugins.apply("java")

    repositories {
        mavenCentral()
        maven("https://redempt.dev")
    }
}