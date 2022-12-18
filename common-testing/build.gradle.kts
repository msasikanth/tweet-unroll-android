plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlinter)
}

android {
  namespace = "dev.sasikanth.twine.common.testing"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testInstrumentationRunner = "dev.sasikanth.twine.common.testing.di.TwineTestRunner"
    consumerProguardFiles("consumer-rules.pro")

    manifestPlaceholders["appAuthRedirectScheme"] = "empty"
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

hilt {
  enableAggregatingTask = true
}

kapt {
  correctErrorTypes = true
}

dependencies {
  implementation(projects.auth)
  implementation(projects.data)
  implementation(projects.common)

  implementation(libs.androidx.core)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)

  implementation(libs.kotlinx.coroutines)

  implementation(libs.paging.common)

  api(libs.junit)
  api(libs.androidx.test.junit)
  api(libs.androidx.test.espresso)
  api(libs.hilt.test)
  api(libs.kotlinx.coroutines.test)
  api(libs.truth)
  api(libs.turbine)
}
