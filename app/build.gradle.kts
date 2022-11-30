plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlinter)
}

android {
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    applicationId = "dev.sasikanth.twine"
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()
    versionCode = 1
    versionName = "1.0.0-pre-alpha-01"

    testInstrumentationRunner = "dev.sasikanth.twine.common.testing.di.TwineTestRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true

      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      val clientId = System.getenv("TWINE_PROD_CLIENT_ID")
      val clientSecret = System.getenv("TWINE_PROD_CLIENT_SECRET")

      buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
      buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")
    }

    debug {
      val clientId = System.getenv("TWINE_DEV_CLIENT_ID")
      val clientSecret = System.getenv("TWINE_DEV_CLIENT_SECRET")

      buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
      buildConfigField("String", "CLIENT_SECRET", "\"$clientSecret\"")

      applicationIdSuffix = ".debug"
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
  namespace = "dev.sasikanth.twine"
}

kapt {
  correctErrorTypes = true
}

hilt {
  enableAggregatingTask = true
}

dependencies {
  implementation(projects.auth)
  implementation(projects.common)
  implementation(projects.data)
  implementation(projects.uiCommon)
  implementation(projects.uiLogin)
  implementation(projects.uiHome)
  implementation(projects.uiSettings)

  implementation(libs.androidx.activity)
  implementation(libs.androidx.core)
  implementation(libs.bundles.compose)
  implementation(libs.bundles.androidx.lifecycle)

  implementation(libs.hilt)
  kapt(libs.hilt.compiler)

  implementation(libs.bundles.retrofit)
  implementation(libs.moshi)
  debugImplementation(libs.okhttp.logging)

  implementation(libs.work.hilt)

  val firebaseBom = platform(libs.firebase.bom)
  implementation(firebaseBom)
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.crashlytics)

  debugImplementation(libs.leakCanary)

  testImplementation(libs.junit)
  testImplementation(projects.commonTesting)

  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.espresso)
  androidTestImplementation(libs.compose.ui.test)
  debugImplementation(libs.compose.ui.tooling)
  debugImplementation(libs.compose.ui.test.manifest)

  androidTestImplementation(libs.hilt.test)
  kaptAndroidTest(libs.hilt.compiler)

  androidTestImplementation(projects.commonTesting)
}

if (file("google-services.json").exists()) {
  apply(plugin = libs.plugins.google.services.get().pluginId)
  apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}
