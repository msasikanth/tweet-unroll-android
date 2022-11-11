package dev.sasikanth.twine

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CanaryTest {

  @Test
  fun canary_test() {
    assertThat(true).isTrue()
  }
}
