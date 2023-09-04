plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "6.6-rc1"
    id("io.papermc.paperweight.userdev") version "1.5.4" // Paper
}

group = "net.premierstudios"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven { url = uri("https://s01.oss.sonatype.org/content/groups/public/") } // RyseInventory
}

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.2-R0.1-SNAPSHOT") // Paper
    implementation("io.github.rysefoxx.inventory:RyseInventory-Plugin:1.5.7") // RyseInventory
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4") // MariaDB
    implementation("com.zaxxer:HikariCP:4.0.3") // HikariCP
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("C:/Users/estev/OneDrive/Área de Trabalho/Devolper/Minecraft/Servidores Testes/1.19.2/plugins/${project.name}-${project.version}.jar"))
    }
}