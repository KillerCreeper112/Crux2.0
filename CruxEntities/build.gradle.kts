
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly(project(":CruxAttributes"))
    compileOnly(project(":CruxWorlds"))
    //todo for some reason gradle won't compile unless these are here.... .___.
    compileOnly(project(":CruxExternal"))
    compileOnly(project(":CruxGeneration"))

    compileOnly(fileTree("libs") {
        include("*.jar")
    })
}
tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}