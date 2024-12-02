plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.farmermarket"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.farmermarket"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.ui:ui-graphics:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-database:21.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


    implementation ("com.google.accompanist:accompanist-pager:0.27.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.27.1")

    implementation("com.tbuonomo:dotsindicator:5.0")

    implementation ("androidx.compose.foundation:foundation:1.6.8")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation ("androidx.compose.material:material-icons-extended:1.7.3")

    implementation("io.coil-kt:coil-compose:2.4.0")

    //ktor

    implementation ("io.ktor:ktor-client-core:2.3.2")
    implementation ("io.ktor:ktor-client-cio:2.3.2") // or another engine like OkHttp
    implementation ("io.ktor:ktor-client-websockets:2.3.2")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    // Ktor Client dependencies
    implementation("io.ktor:ktor-client-core:2.3.0")   // Core Ktor Client
    implementation("io.ktor:ktor-client-websockets:2.3.0")  // WebSocket support
    implementation("io.ktor:ktor-client-cio:2.3.0")  // CIO Engine for Ktor Client
    implementation("io.ktor:ktor-client-json:2.3.0")  // JSON support for Ktor Client
    implementation("io.ktor:ktor-client-serialization:2.3.0")  // Serialization support for Ktor Client

    // OkHttp dependency (if you need OkHttp for other purposes)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")  // or the version you prefer
    implementation ("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")


    implementation ("com.google.firebase:firebase-database:20.1.1")




}


