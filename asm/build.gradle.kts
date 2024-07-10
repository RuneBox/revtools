plugins {
    kotlin("jvm")
    `maven-publish`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("org.ow2.asm:asm:_")
    api("org.ow2.asm:asm-commons:_")
    api("org.ow2.asm:asm-util:_")
    api("org.ow2.asm:asm-tree:_")
    implementation("com.google.guava:guava:_")
    implementation("org.soot-oss:sootup.core:_")
    implementation("org.soot-oss:sootup.java.core:_")
    implementation("org.soot-oss:sootup.java.sourcecode:_")
    implementation("org.soot-oss:sootup.java.bytecode:_")
    implementation("org.soot-oss:sootup.jimple.parser:_")
    implementation("org.soot-oss:sootup.callgraph:_")
    implementation("org.soot-oss:sootup.analysis:_")
    implementation("org.soot-oss:sootup.qilin:_")
    implementation("org.soot-oss:soot:_")
}
