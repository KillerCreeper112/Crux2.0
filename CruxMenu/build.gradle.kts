
version = "1.0"

plugins{
    alias(libs.plugins.paperweight)
}

dependencies {
    compileOnly(project(":CruxMain"))
    paperweight.paperDevBundle(libs.versions.paper)
}
