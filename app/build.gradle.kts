import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kmobile.museointeractivo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kmobile.museointeractivo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        val props = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) {
                file.inputStream().use { load(it) }
            }
        }

        buildConfigField("String", "PODCASTINDEX_KEY", "\"${props.getProperty("PODCASTINDEX_KEY", "")}\"")
        buildConfigField("String", "PODCASTINDEX_SECRET", "\"${props.getProperty("PODCASTINDEX_SECRET", "")}\"")
        buildConfigField("String", "PODCASTINDEX_UA", "\"${props.getProperty("PODCASTINDEX_UA", "MuseoInteractivo")}\"")
        buildConfigField("String", "VIDEO_KEY", "\"${props.getProperty("VIDEO_KEY", "")}\"")

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
        buildConfig = true
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer.hls)
    //coil
    implementation(libs.coil)
    implementation(libs.coil.compose) // si usas Compose
    //Corutinas
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //navegacion
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("net.engawapg.lib:zoomable:1.6.1")

    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")


}