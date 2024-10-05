
version = "1.0"

plugins{
    alias(libs.plugins.paperweight)
}

dependencies {
    compileOnly(project(":CruxMain"))
    compileOnly(libs.crunch)
    //compileOnly("org.jetbrains:annotations.24.0.1")
    /*compileOnly("org.jetbrains", "annotations", "24.0.1")
    compileOnly("com.google.code.gson", "gson", "2.10.1")*/
    paperweight.paperDevBundle(libs.versions.paper)
    //<groupId>org.jetbrains</groupId>
    //    <artifactId>annotations</artifactId>
    //    <version>16.0.1</version>
}
tasks.getByName<JavaCompile>("compileJava") {
    dependsOn(":CruxMain:shadowJar")
}
