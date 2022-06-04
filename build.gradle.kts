plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.1.2")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.github.koca2000:NoteBlockAPI:1.6.1")
    implementation("fr.mrmicky:fastboard:1.2.1")
    implementation("org.jetbrains:annotations:23.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}