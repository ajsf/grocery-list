plugins {
    val kotlinVersion = "1.2.21"
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
}

apply {
    plugin("org.springframework.boot")
}

dependencies {
    val springBootVersion: String = parent!!.properties["springBootVersion"] as String
    val hibernateValidatorVersion: String = parent!!.properties["hibernateValidatorVersion"] as String
    val kotlinxHtmlVersion: String = properties["kotlinxHtmlVersion"] as String
    compile(project(":backend:user"))
    compile(project(":backend:groceries"))
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4")
    compile("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    compile("org.springframework.boot:spring-boot-devtools:$springBootVersion")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
    compile("org.hibernate:hibernate-validator:$hibernateValidatorVersion")
    testCompile("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testCompile("io.projectreactor:reactor-test:3.1.4.RELEASE")
}
