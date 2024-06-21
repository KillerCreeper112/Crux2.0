
version = "1.0"

plugins{
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
    compileOnly(project(":CruxConfigs"))
}

tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}
