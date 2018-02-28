plugins {
    base
}

allprojects {
    group = "com.doublea"
    version = "1.0"
    repositories {
        jcenter()
    }
}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}