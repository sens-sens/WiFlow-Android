plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.0"

}

android {
    namespace = "com.androsmith.wiflow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androsmith.wiflow"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources. excludes. add("META-INF/DEPENDENCIES")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // FTP Server
    implementation(libs.apache.ftpserver.core)

    implementation("androidx.core:core-splashscreen:1.0.1")


    implementation("androidx.documentfile:documentfile:1.0.1") // Or latest version

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    val nav_version = "2.8.5"

    implementation("androidx.navigation:navigation-compose:$nav_version")


        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


    implementation("org.apache.ftpserver:ftpserver-core:1.2.0")
    implementation("org.apache.ftpserver:ftplet-api:1.2.0")
    implementation("org.apache.mina:mina-core:2.1.6")

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}