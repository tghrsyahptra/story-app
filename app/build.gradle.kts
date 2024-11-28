plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tghrsyahptra.storyapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tghrsyahptra.storyapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        buildConfig = true // Aktifkan fitur BuildConfig
        viewBinding = true
    }
}

dependencies {
    // Core Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Networking and Image Loading
    implementation(libs.retrofit) // Retrofit Core
    implementation(libs.converter.gson) // Gson Converter
    implementation(libs.logging.interceptor) // OkHttp Logging
    implementation(libs.glide) // Glide

    // Lifecycle Components
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx) // LiveData
    implementation(libs.androidx.datastore.preferences) // DataStore Preferences

    // CameraX Dependencies
    implementation(libs.camera.core) // Core CameraX library
    implementation(libs.camera.camera2) // Camera2 interop
    implementation(libs.camera.lifecycle) // Lifecycle-aware CameraX
    implementation(libs.camera.view) // CameraView for preview
    implementation(libs.camera.extensions) // Extensions for CameraX
    implementation(libs.fragment.ktx) // Fragment with Kotlin extensions

    // Activity Lifecycle Handling for CameraX
    implementation(libs.androidx.activity.ktx.v180)
}