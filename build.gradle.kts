// Top-level build file
plugins {
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.android.application") version "8.4.0" apply false
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()

    }
}
