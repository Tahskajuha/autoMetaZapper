val appName = "autoMetaZapper"
val packageName = "com.example.autometazapper"

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = packageName
  compileSdk = 36

  defaultConfig {
    applicationId = packageName
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  kotlinOptions {
    jvmTarget = "21"
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.17.0")
  implementation("androidx.appcompat:appcompat:1.7.1")
  implementation("org.apache.commons:commons-imaging:1.0.0-alpha6")
}
