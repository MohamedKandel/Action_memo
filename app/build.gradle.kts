plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mkandeel.actionmemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mkandeel.actionmemo"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    // data store dependency
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // ViewModel
    val latest_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$latest_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$latest_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$latest_version")
    implementation("androidx.core:core-ktx:1.12.0")
}