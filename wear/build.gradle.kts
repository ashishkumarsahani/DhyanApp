plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.epilepto.dhyanapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.epilepto.dhyanapp"
        minSdk = 30
        targetSdk = 33
        versionCode = 2
        versionName = "1.0"
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.wear.compose:compose-material:1.2.1")
    implementation("androidx.wear.compose:compose-foundation:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.wear.tiles:tiles:1.2.0")
    implementation("androidx.wear.tiles:tiles-material:1.2.0")
    implementation("com.google.android.horologist:horologist-compose-tools:0.5.9")
    implementation("com.google.android.horologist:horologist-tiles:0.5.10")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.0")
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")
    implementation ("androidx.wear.compose:compose-navigation:1.2.1")
//    implementation("androidx.navigation:navigation-compose:2.4.0-alpha02")
//    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
//    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
//    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("androidx.compose.runtime:runtime-livedata:1.5.4")

    implementation ("androidx.media3:media3-exoplayer:1.2.0")
    implementation ("androidx.media3:media3-ui:1.2.0")
    implementation ("androidx.media3:media3-common:1.2.0")
    implementation ("com.google.android.horologist:horologist-media-ui:0.5.10")
    implementation ("com.google.android.horologist:horologist-media-data:0.5.10")
    implementation ("com.google.android.horologist:horologist-audio-ui:0.5.10")
    implementation ("com.google.android.horologist:horologist-audio:0.5.10")
    implementation ("androidx.wear:wear:1.3.0")
}