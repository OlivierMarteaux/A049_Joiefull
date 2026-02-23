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
        versionName = "1.0.0"

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

    // Add JVM toolchain to define global java version
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
    implementation(libs.androidx.material3.window.size.class1.android) //AdaptiveLayout
    implementation (libs.androidx.material.icons.extended) // icons
    implementation(libs.androidx.core.splashscreen) // splashscreen

    /** Koin for DI */
    implementation(libs.koin.android) // Core Koin for Android
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)// Koin for Compose

    /** Ktor for web API */
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android) // Android engine
    implementation(libs.ktor.client.content.negotiation) // Serialization support
    implementation(libs.ktor.serialization.kotlinx.json) // Kotlinx JSON

    /** Unit tests */
    testImplementation(libs.okhttp3.mockwebserver) // MockWebServer for API testing
    testImplementation(libs.kotlinx.coroutines.test) // coroutine test (runTest)
    testImplementation(libs.mockito.kotlin) // Mockito mocking framework
    testImplementation(libs.mockk) // kotlin mocking framework
    testImplementation(libs.turbine)// Flow test

    /** debug conf to allow preview in AndroidStudio */
    testImplementation(libs.ktor.client.mock)// For MockEngine + respond
    debugImplementation(libs.ui.tooling)
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