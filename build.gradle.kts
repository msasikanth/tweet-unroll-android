@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application).apply(false)
  alias(libs.plugins.android.library).apply(false)
  alias(libs.plugins.kotlin.android).apply(false)
  alias(libs.plugins.kotlin.kapt).apply(false)
  alias(libs.plugins.hilt).apply(false)
  alias(libs.plugins.ksp).apply(false)
  alias(libs.plugins.google.services).apply(false)
  alias(libs.plugins.firebase.crashlytics).apply(false)
  alias(libs.plugins.kotlinter).apply(false)
}

buildscript {
  dependencies {
    classpath(libs.compose.twitter.rules)
  }
}

tasks.register("clean") {
  doFirst {
    delete(rootProject.buildDir)
  }
}

subprojects {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"

      if (project.findProperty("twine.enableComposeCompilerReports") == "true") {
        freeCompilerArgs += listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics"
        )
        freeCompilerArgs += listOf(
          "-P",
          "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
            project.buildDir.absolutePath + "/compose_metrics"
        )
      }
    }
  }
}

apply(plugin = "android-reporting")
