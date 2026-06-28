group = "org.ivcode.core"
version = "0.1.0-SNAPSHOT"

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    jacoco
    id("org.ivcode.gradle-publish") version "0.1-SNAPSHOT"
}

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
}

dependencies {
    implementation("com.github.spullara.mustache.java:compiler:0.9.14")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("io.mockk:mockk:1.13.12")
}

gradlePlugin {
    plugins {
        this.create("gradle-dokka-pages", Action {
            id = "org.ivcode.core.gradle-dokka-pages"
            implementationClass = "org.ivcode.core.gradle.dokkapages.DockkaPagesPlugin"
        })
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
tasks.build { dependsOn("jacocoTestReport") }

tasks.test {
    useJUnitPlatform()
}
