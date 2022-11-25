import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
  alias(libs.plugins.android.test)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "dev.sasikanth.twine.benchmark"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    // This benchmark buildType is used for benchmarking, and should function like your
    // release build (for example, with minification on). It"s signed with a debug key
    // for easy local/CI testing.
    create("benchmark") {
      isDebuggable = true
      signingConfig = getByName("debug").signingConfig
      matchingFallbacks += listOf("release")
    }
  }

  testOptions {
    managedDevices {
      devices {
        create ("pixel6Api31", ManagedVirtualDevice::class) {
          device = "Pixel 6"
          apiLevel = 31
          systemImageSource = "aosp"
        }
      }
    }
  }

  targetProjectPath = ":app"
  experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
  implementation(libs.androidx.test.junit)
  implementation(libs.androidx.test.espresso)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.junit)
}

androidComponents {
  beforeVariants(selector().all()) {
    it.enable = it.buildType == "benchmark"
  }
}
