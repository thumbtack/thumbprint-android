buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.13.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.18.0")
}

detekt {
    toolVersion = "1.18.0"
    config = files("config/detekt/detekt.yml")
}
