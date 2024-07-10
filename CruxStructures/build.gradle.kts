
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

repositories{
    maven("https://jitpack.io")
}

tasks{
    assemble{
        dependsOn(shadowJar)
    }
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly((project(":CruxConfigs")))

    compileOnly(fileTree("libs") {
        include("*.jar")
    })
}
tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}