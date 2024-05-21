import java.util.Properties

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

val vbox_sdk_version: String by lazy {
  val inputStream = File("${rootProject.projectDir}/app/vbox_sdk_version.properties").inputStream()
  val properties = Properties()
  properties.load(inputStream)
  inputStream.close()
  properties.getProperty("vbox_sdk_version")
}

dependencies {
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.12.0")
  implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
  implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
  implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
  implementation(files("../sdk/vsystem_${vbox_sdk_version}.aar"))
  compileOnly(files("../sdk/fake_api_${vbox_sdk_version}.jar"))
}