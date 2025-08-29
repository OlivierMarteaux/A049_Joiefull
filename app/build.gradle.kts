import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)  // for Kotlin serialization for Retrofit
}

android {
    namespace = "com.oliviermarteaux.a049_joiefull"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.oliviermarteaux.a049_joiefull"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // Replaced by JVM toolchain setting
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    // Add JVM tooolchain to define global java version
    kotlin { jvmToolchain(17) }

    buildFeatures {
        compose = true
    }
}

dependencies {

    /** Personal shared library */
    implementation(libs.oliviermarteaux.compose)
    implementation(libs.oliviermarteaux.core)

    /** Base dependencies */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)

    /** Coil for image loading */
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp) // to load images from internet

    /** Android Compose*/
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)// Navigation
    implementation (libs.androidx.material.icons.extended)

    /** Koin for DI */
    implementation(libs.koin.android) // Core Koin for Android
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)// Koin for Compose

    /** Ktor for web API */
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android) // Android engine
    implementation(libs.ktor.client.content.negotiation) // Serialization support
    implementation(libs.ktor.serialization.kotlinx.json) // Kotlinx JSON

    /** Retrofit for web API */
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)

    /** Unit tests */
    testImplementation(libs.okhttp3.mockwebserver) // MockWebServer for API testing
    testImplementation(libs.kotlinx.coroutines.test) // coroutine test (runTest)
    testImplementation(libs.mockito.kotlin) // Mockito mocking framework

    /** debug conf to allow preview in AndroidStudio */
    testImplementation(libs.ktor.client.mock)
    debugImplementation(libs.ui.tooling) // For MockEngine + respond

    implementation (libs.androidx.material.icons.extended)

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//
//    implementation(libs.material3)//material3 for custom ripple effect
//    implementation(libs.androidx.activity.ktx)// activity dependency for Google ImagePicker
//    implementation(libs.google.accompanist.systemuicontroller)// Google accompanist for SystemUiController
//    implementation(libs.material)// google material for icons
//    implementation(libs.androidx.navigation.compose)// Navigation
//
    /** Coil for image viewing */
//    implementation(libs.coil.compose)
//    implementation(libs.coil.network.okhttp)
//    implementation(libs.coil.video)
//
    /** room */
//    implementation(libs.androidx.room.runtime)
//    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
//    ksp(libs.androidx.room.compiler)
//    // If this project only uses Java source, use the Java annotationProcessor
//    // No additional plugins are necessary
//    // annotationProcessor(libs.androidx.room.compiler)
//    // optional - Kotlin Extensions and Coroutines support for Room
//    implementation(libs.androidx.room.ktx)
//
    /** Retrofit for currency conversion API */
//    implementation(libs.retrofit2.kotlinx.serialization.converter)
//    implementation(libs.retrofit)
//    implementation(libs.okhttp)
//    implementation(libs.kotlinx.serialization.json)
//
//    testImplementation(libs.junit)
//    testImplementation(libs.mockk) // kotlin mocking framework
//    testImplementation(libs.kotlinx.coroutines.test) // coroutine test (runTest)
//    testImplementation(libs.okhttp3.mockwebserver)// MockWebServer for API testing
//    testImplementation(libs.mockito.kotlin)// Mockito
//
//    androidTestImplementation(libs.turbine)// Flow test
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
}

/**
 * Helper function to force application run to be performed on physical device.
 */
val targetDevice = "adb-cb4d0d70-D3BuA7._adb-tls-connect._tcp" // ← Replace with your actual device ID
val checkPhysicalDevice = tasks.register("checkPhysicalDevice") {
    doFirst {
        val adbOutput = ProcessBuilder("adb", "devices")
            .redirectErrorStream(true)
            .start()
            .inputStream
            .bufferedReader()
            .readText()

        val connectedDevices = adbOutput.lines().filter { line ->
            line.isNotBlank() &&
                    !line.startsWith("List") &&
                    line.contains("device") &&
                    !line.startsWith("emulator-")
        }

        if (connectedDevices.none { it.startsWith(targetDevice) }) {
            throw GradleException("ERROR: Required physical device ($targetDevice) is not connected.")
        } else {
            println("✅ Physical device ($targetDevice) is connected. Proceeding with build.")
        }
    }
}