
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly(fileTree("libs") {
        include("*.jar")
    })
}

tasks{
    compileJava{
        //todo this fucking thing dependsOn(project(":CruxMain").tasks.named("shadowJar"))
    }
}
