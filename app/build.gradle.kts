plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp") version "2.3.12"

    // Firebase - Google Services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.fundaciontextilcircular"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.fundaciontextilcircular"

        minSdk = 24
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            optimization {
                enable = true

                packageScope =
                    setOf(
                        "androidx.**",
                        "kotlin.**",
                        "kotlinx.**"
                    )
            }
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // =========================
    // COMPOSE
    // =========================

    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    implementation(
        platform(libs.androidx.compose.bom)
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )


    // =========================
    // VIEWMODEL
    // =========================

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
    )


    // =========================
    // UBICACION
    // =========================

    implementation(
        "com.google.android.gms:play-services-location:21.3.0"
    )


    // =========================
    // ROOM
    // =========================

    implementation(
        "androidx.room:room-runtime:2.8.5"
    )

    implementation(
        "androidx.room:room-ktx:2.8.5"
    )

    ksp(
        "androidx.room:room-compiler:2.8.5"
    )


    // =========================
    // FIREBASE
    // =========================

    implementation(
        platform(
            "com.google.firebase:firebase-bom:34.12.0"
        )
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )


    // =========================
    // TEST
    // =========================

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}