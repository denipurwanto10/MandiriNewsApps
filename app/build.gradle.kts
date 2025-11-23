plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.mandirinews"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mandirinews"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Retrofit Base URL
        buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("Boolean", "DEBUG", "true")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "DEBUG", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // Android UI Components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)                 // ✔ Material Components (BENAR)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Glide (Image Loader)
    implementation(libs.glide)

    // Retrofit + Gson Converter
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // OkHttp Logging Interceptor
    implementation(libs.logging.interceptor)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Android Architecture Components (Lifecycle)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ❌ DELETE THIS (duplikasi & salah)
    implementation(libs.google.material)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
