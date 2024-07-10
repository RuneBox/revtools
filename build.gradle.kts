plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

allprojects {
    group = "io.runebox.revtools"
    version = "1.0.0"

    repositories {
        mavenCentral()
        mavenLocal()
    }
}

val nonKotlinProjects = listOf(":asm")

configure(subprojects.filterNot { it.name in nonKotlinProjects }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
    }

    val sourcesJar = tasks.register("sourcesJar", Jar::class) {
        group = "build"
        archiveClassifier.set("sources")
        from(sourceSets["main"].allJava)
    }

    publishing {
        repositories {
            mavenLocal()
        }

        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = group.toString()
                artifactId = project.name
                version = project.version.toString()
                afterEvaluate {
                    artifact(sourcesJar.get())
                }
            }
        }
    }

    artifacts {
        add("implementation", sourcesJar.get())
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
        implementation.set(JvmImplementation.J9)
    }
}

kotlin {
    jvmToolchain(11)
}

tasks.wrapper {
    gradleVersion = "8.7"
}