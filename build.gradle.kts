import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id ("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

val minecraftVersion = "1.21.1"
val plversion = "1.0-SNAPSHOT"

group = "de.luca"
version = plversion

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    paperweight.paperDevBundle("$minecraftVersion-R0.1-SNAPSHOT")
}

tasks {
    runServer {
        minecraftVersion(minecraftVersion)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paper {
    name = "DeathMinigames"
    version = plversion
    main = "org.example.DeathMinigames.deathMinigames.Main"
    apiVersion = "1.21"
}