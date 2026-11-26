import org.gradle.testing.jacoco.tasks.JacocoReport


plugins {

    alias(
        libs.plugins.android.application
    )

    alias(
        libs.plugins.kotlin.compose
    )

    id(
        "com.google.devtools.ksp"
    ) version "2.3.12"

    // Firebase - Google Services
    id(
        "com.google.gms.google-services"
    )

    // Cobertura de pruebas
    jacoco
}


android {

    namespace =
        "com.example.fundaciontextilcircular"


    compileSdk {

        version =
            release(
                37
            )
    }


    defaultConfig {

        applicationId =
            "com.example.fundaciontextilcircular"


        minSdk =
            24


        targetSdk =
            37


        versionCode =
            1


        versionName =
            "1.0"


        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {

        release {

            optimization {

                enable =
                    true


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

        compose =
            true
    }
}


// =========================================
// JACOCO
// =========================================

jacoco {

    toolVersion =
        "0.8.15"
}


dependencies {


    // =========================
    // COMPOSE
    // =========================

    implementation(
        "androidx.compose.material:material-icons-extended"
    )


    implementation(
        platform(
            libs.androidx.compose.bom
        )
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
    // UBICACIÓN
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
    // RETROFIT
    // =========================

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.work:work-runtime-ktx:2.9.1")



    // =========================
    // RETROFIT
    // =========================

    implementation(
        "com.squareup.retrofit2:retrofit:2.11.0"
    )


    implementation(
        "com.squareup.retrofit2:converter-gson:2.11.0"
    )


    implementation(
        "com.squareup.okhttp3:logging-interceptor:4.12.0"
    )



    // =========================
    // TEST UNITARIOS
    // =========================

    testImplementation(
        libs.junit
    )


    testImplementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2"
    )


    testImplementation(
        "io.mockk:mockk:1.14.6"
    )



    // =========================
    // TEST ANDROID
    // =========================

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
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



    // =========================
    // DEBUG
    // =========================

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )


    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}


// =========================================
// CLASES QUE QUEREMOS MEDIR
// =========================================

val clasesViewModel =
    listOf(

        // LOGIN
        "**/LoginViewModel.class",
        "**/LoginViewModel\$*.class",

        // REGISTRO
        "**/RegistroViewModel.class",
        "**/RegistroViewModel\$*.class",

        // ADMIN
        "**/AdminViewModel.class",
        "**/AdminViewModel\$*.class"
    )


// =========================================
// REPORTE DE COBERTURA JACOCO
// =========================================

tasks.register<JacocoReport>(
    "jacocoTestReport"
) {

    group =
        "verification"


    description =
        "Genera cobertura de LoginViewModel, RegistroViewModel y AdminViewModel"


    // Primero ejecuta todas las pruebas
    dependsOn(
        "testDebugUnitTest"
    )


    // =====================================
    // REPORTES
    // =====================================

    reports {

        xml.required.set(
            true
        )


        html.required.set(
            true
        )


        csv.required.set(
            false
        )


        html.outputLocation.set(

            layout
                .buildDirectory
                .dir(
                    "reports/jacoco/viewmodels/html"
                )
        )


        xml.outputLocation.set(

            layout
                .buildDirectory
                .file(
                    "reports/jacoco/viewmodels/jacoco.xml"
                )
        )
    }


    // =====================================
    // CLASES KOTLIN
    // AGP 9 / BUILT-IN KOTLIN
    // =====================================

    val kotlinClasses =
        fileTree(

            layout
                .buildDirectory
                .dir(
                    "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes"
                )
                .get()
                .asFile
        ) {

            include(
                clasesViewModel
            )
        }


    // =====================================
    // CLASES JAVA
    // =====================================

    val javaClasses =
        fileTree(

            layout
                .buildDirectory
                .dir(
                    "intermediates/javac/debug/compileDebugJavaWithJavac/classes"
                )
                .get()
                .asFile
        ) {

            include(
                clasesViewModel
            )
        }


    // =====================================
    // CLASES A ANALIZAR
    // =====================================

    classDirectories.setFrom(

        files(

            kotlinClasses,

            javaClasses
        )
    )


    // =====================================
    // CÓDIGO FUENTE
    // =====================================

    sourceDirectories.setFrom(

        files(

            "src/main/java",

            "src/main/kotlin"
        )
    )


    additionalSourceDirs.setFrom(

        files(

            "src/main/java",

            "src/main/kotlin"
        )
    )


    // =====================================
    // DATOS GENERADOS POR LOS TESTS
    // =====================================

    executionData.setFrom(

        fileTree(

            layout
                .buildDirectory
                .get()
                .asFile
        ) {

            include(
                "jacoco/testDebugUnitTest.exec"
            )


            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
            )


            include(
                "**/testDebugUnitTest.exec"
            )
        }
    )
}