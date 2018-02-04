allprojects {
    group = "com.doublea"
    version = "1.0"
    repositories {
        jcenter()
    }
}

plugins {
    base
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}