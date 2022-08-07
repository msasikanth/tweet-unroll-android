plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
}

android {
  namespace = "dev.sasikanth.twine.common.testing"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testInstrumentationRunner = "dev.sasikanth.twine.common.testing.di.TwineTestRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

kapt {
  correctErrorTypes = true
}

dependencies {
  implementation(libs.androidx.core)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlinx.coroutines)

  api(libs.junit)
  api(libs.androidx.test.junit)
  api(libs.androidx.test.espresso)
  api(libs.hilt.test)
  api(libs.kotlinx.coroutines.test)
  api(libs.truth)
}
