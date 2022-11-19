package dev.sasikanth.twine.common.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class SwitchTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun switch_should_work_correctly() {
    // given
    var checked by mutableStateOf(false)

    composeTestRule.setContent {
      TwineTheme {
        Switch(
          checked = checked,
          onValueChange = {
            checked = it
          }
        )
      }
    }

    assertThat(checked).isEqualTo(false)

    // when
    composeTestRule
      .onRoot()
      .performClick()

    // then
    assertThat(checked).isEqualTo(true)
  }

  @Test
  fun disabling_switch_should_work_correctly() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        Switch(
          checked = false,
          enabled = false,
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Switch")
      .assertIsNotEnabled()
  }

  @Test
  fun enabling_switch_should_work_correctly() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        Switch(
          checked = false,
          enabled = true,
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Switch")
      .assertIsEnabled()
      .assertHasClickAction()
  }
}
