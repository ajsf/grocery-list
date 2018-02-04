plugins {
    id("org.jetbrains.kotlin.plugin.spring") version embeddedKotlinVersion
}

apply {
    plugin("org.springframework.boot")
}

dependencies {
    val springBootVersion: String = parent!!.properties["springBootVersion"] as String
    val hibernateValidatorVersion: String = parent!!.properties["hibernateValidatorVersion"] as String
    val kotlinxHtmlVersion: String = properties["kotlinxHtmlVersion"] as String
    compile(project(":backend:groceries"))
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
    compile("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    compile("org.springframework.boot:spring-boot-devtools:$springBootVersion")
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
    compile("org.hibernate:hibernate-validator:$hibernateValidatorVersion")
}