plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.hobbyhub"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hobbyhub"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // This is compatible with Kotlin 1.9.23
    }
}

dependencies {
    // Force update to newer kotlinx-metadata-jvm that supports Kotlin 1.9+
    implementation(libs.kotlinx.metadata.jvm) // <-- THE CRUCIAL FIX

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // This line explicitly adds the icon library to your app
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.play.services.location)

    // Google Maps
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val nav_version = "2.9.5"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Hilt Dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // ADDED: Dependency for hiltViewModel() function in Compose Navigation
    implementation(libs.androidx.hilt.navigation.compose)


    // 1. Image Loading (Coil)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // 2. Image Cropping Library (FIXED: Using correct GitHub/JitPack coordinates)
    implementation("com.github.ArthurHub:Android-Image-Cropper:2.8.0")

    // 3. Accompanist Flow Layout
    implementation("com.google.accompanist:accompanist-flowlayout:0.32.0")

    // Activity Result API for picking and cropping
    implementation("androidx.activity:activity-compose:1.8.1")

    // 🔑 FIXED: Explicitly add appcompat to provide the required XML themes
    implementation("androidx.appcompat:appcompat:1.6.1")


    //Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    //ktor-client
    implementation("io.ktor:ktor-client-android:3.3.1")
}