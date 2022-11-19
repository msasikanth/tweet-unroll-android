import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.kapt)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlinter)
}

android {
  namespace = "dev.sasikanth.twine.data"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testInstrumentationRunner = "dev.sasikanth.twine.common.testing.di.TwineTestRunner"
    testInstrumentationRunnerArguments["notAnnotation"] = "androidx.test.filters.FlakyTest"
    consumerProguardFiles("consumer-rules.pro")

    val testTwineBearerToken = System.getenv("TWINE_DEV_BEARER_TOKEN")
    buildConfigField("String", "TEST_TWINE_BEARER_TOKEN", "\"$testTwineBearerToken\"")

    manifestPlaceholders["appAuthRedirectScheme"] = "empty"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}

kapt {
  correctErrorTypes = true
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
  implementation(projects.common)

  implementation(libs.androidx.core)
  implementation(libs.hilt)
  kapt(libs.hilt.compiler)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.bundles.retrofit)
  implementation(libs.moshi)
  ksp(libs.moshi.codegen)

  implementation(libs.bundles.room)
  ksp(libs.room.compiler)

  implementation(libs.androidx.datastore.pref)

  implementation(libs.work.runtime)
  implementation(libs.work.hilt)
  kapt(libs.work.hilt.compiler)

  implementation(libs.androidx.lifecycle.livedata.ktx)

  androidTestImplementation(projects.commonTesting)
  kaptAndroidTest(libs.hilt.compiler)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.work.testing)

  coreLibraryDesugaring(libs.desugar.jdk.libs)
}
