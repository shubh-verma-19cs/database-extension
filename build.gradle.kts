plugins {
    id("com.hivemq.extension")
}

group = "com.hivemq.extensions"
description = "HiveMQ SQL Database Extension"
version = "1.0-SNAPSHOT"

hivemqExtension{
    name = "HiveMQ Database Extension"
    author = "Shubh Verma"
    mainClass = "org.example.DatabaseExtensionMain"

//
//    priority = 1000
}

repositories {
    mavenCentral()
}

dependencies {
    hivemqProvided("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("commons-dbutils:commons-dbutils:1.7")
    implementation("org.postgresql:postgresql:42.1.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
//
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
//
//tasks.prepareHivemqHome{
//    hivemqFolder.set("/home/coded/Workspace/hivemq-4.8.4/")
//}