
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly(project(":CruxConfigs"))
    compileOnly(project(":CruxItems"))
    compileOnly(project(":CruxBlocks"))
}

tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}