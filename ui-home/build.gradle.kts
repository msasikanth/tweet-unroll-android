plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlinter)
}

android {
  namespace = "dev.sasikanth.twine.home"
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
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  packagingOptions {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }
}

hilt {
  enableAggregatingTask = true
}

dependencies {
  implementation(projects.uiCommon)
  implementation(projects.data)
  implementation(projects.common)

  implementation(libs.androidx.core)
  implementation(libs.bundles.androidx.lifecycle)
  implementation(libs.bundles.compose)
  implementation(libs.androidx.activity)

  implementation(libs.kotlin.stdlib)

  implementation(libs.hilt)
  implementation(libs.hilt.navigation.compose)
  kapt(libs.hilt.compiler)

  implementation(libs.bundles.paging)

  implementation(libs.coil.compose)

  testImplementation(libs.junit)
  testImplementation(projects.commonTesting)
  testImplementation(libs.paging.common)

  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.espresso)
  androidTestImplementation(libs.compose.ui.test)
  debugImplementation(libs.compose.ui.tooling)
  debugImplementation(libs.compose.ui.test.manifest)
  androidTestImplementation(libs.truth)
}
