
version = "1.0"
plugins {
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)
    compileOnly(project(":CruxMain"))
}

tasks{
    compileJava{
        dependsOn(project(":CruxMain").tasks.named("shadowJar"))
        //dependsOn project(':CruxMain').tasks.named('shadowJar')
    }
}