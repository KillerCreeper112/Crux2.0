
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

repositories{
    //maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly(project(":CruxConfigs"))
    compileOnly(project(":CruxMenus"))
    compileOnly(project(":CruxStats"))
    //compileOnly("com.github.ZockerAxel", "CrazyAdvancementsAPI", "2.1.19")

    compileOnly(fileTree("libs") {
        include("*.jar")
    })
}
tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}