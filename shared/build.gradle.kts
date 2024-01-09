import com.android.build.api.dsl.Packaging

val coroutinesVersion = "1.7.3"
val ktorVersion = "2.3.5"
val sqlDelightVersion = "1.5.5"
val dateTimeVersion = "0.4.1"
val lifecycle_version = "2.6.2"
val material_version = "1.5.4"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization").version("1.9.21")
//    id("com.squareup.sqldelight").version("1.5.5")
    id("dev.icerock.moko.kswift").version("0.6.1")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            implementation("io.ktor:ktor-utils:$ktorVersion")
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//            implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")
            implementation("de.cketti.unicode:kotlin-codepoints:0.6.1")
            implementation("dev.icerock.moko:kswift-runtime:0.6.1")
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:$ktorVersion")
//            implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")

        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
//            implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

kswift {
    iosDeploymentTarget.set("17.0")
    install(dev.icerock.moko.kswift.plugin.feature.PlatformExtensionFunctionsFeature)
//    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
//    excludeLibrary("kotlinx-coroutines-core")
//    excludeLibrary("ktor-utils")
//    excludeLibrary("ktor-client-core")
//    excludeLibrary("ktor-client-content-negotiation")
//    excludeLibrary("ktor-serialization-kotlinx-json")
//    excludeLibrary("io_ktor_ktor-serialization-kotlinx")
//    excludeLibrary("runtime")
//    excludeLibrary("kotlinx-datetime")
//    excludeLibrary("kotlin-codepoints")
//    excludeLibrary("kswift-runtime")
}

android {
    namespace = "com.brettm.holiday"
    compileSdk = 34
    defaultConfig {
        minSdk = 28
    }
}
