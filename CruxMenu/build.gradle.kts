
version = "1.0"

plugins{
    alias(libs.plugins.paperweight)
}

dependencies {
    compileOnly(project(":CruxMain"))
    compileOnly(project(":CruxConfig"))
    paperweight.paperDevBundle(libs.versions.paper)
}
