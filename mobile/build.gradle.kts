plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.epilepto.dhyanapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.epilepto.dhyanapp"
        minSdk = 26
        targetSdk = 33
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    wearApp(project(":wear"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")

    // Runtime
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation ("androidx.compose.runtime:runtime-livedata")

    //Splash Api
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.48.1")
    ksp ("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Coil Library
    implementation ("io.coil-kt:coil-compose:2.5.0")

    //Pager
    implementation ("com.google.accompanist:accompanist-pager:0.27.0")

    //Desugar JDK
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //Date Time Picker
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

//    //Message Bar
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")
//
//    //One Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.11")

    //Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Google Auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
//    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
//    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
//    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")



    //Custom Date Time Picker
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.0.2")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:clock:1.0.2")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.2")

    // Pager Compose
    implementation ("com.google.accompanist:accompanist-pager:0.27.0")
    // Pager Indicator
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.23.1")

    //Palette
    implementation ("androidx.palette:palette-ktx:1.0.0")

    //Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Add the dependencies for the Firebase Cloud Messaging and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    //Horologist (These all 3 libraries are making the configuration of mobile not compatible for Phone version
//    implementation ("com.google.android.horologist:horologist-auth-composables:0.5.8")
//    implementation ("com.google.android.horologist:horologist-auth-ui:0.5.8")
//    implementation ("com.google.android.horologist:horologist-compose-material:0.5.8")


    implementation ("com.google.android.horologist:horologist-datalayer-phone:0.5.9")
    implementation ("com.google.android.horologist:horologist-datalayer:0.5.9")

    //Wearable
    implementation ("com.google.android.gms:play-services-wearable:18.1.0")

    //Y Charts
    implementation("co.yml:ycharts:2.1.0")

    implementation("com.patrykandpatrick.vico:compose:1.12.0")
    implementation("com.patrykandpatrick.vico:compose-m3:1.12.0")
    implementation("com.patrykandpatrick.vico:core:1.12.0")

    //Compose to imageview
    implementation ("dev.shreyaspatil:capturable:1.0.3")
}