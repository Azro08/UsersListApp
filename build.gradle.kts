plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hiltAndroid) apply false
}

buildscript {
    dependencies {
        classpath(libs.hilt.gradle)
    }
}
