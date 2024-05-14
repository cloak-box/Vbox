plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.black.cat.system.demo"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.black.cat.system.demo"
    minSdk = 26
    targetSdk = 30
    versionCode = 1
    versionName = "1.0"

  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig =signingConfigs.maybeCreate("debug")
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
    viewBinding =  true
  }
    lintOptions {
      isCheckReleaseBuilds =false
      // Or, if you prefer, you can continue to check for errors in release builds,
      // but continue the build even when errors are found:
      isAbortOnError= false
    }

}

dependencies {
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.12.0")
  implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
  implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
  implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
  implementation(files("./libs/vsystem.aar"))
  compileOnly(files("./libs/fake_api.jar"))
}