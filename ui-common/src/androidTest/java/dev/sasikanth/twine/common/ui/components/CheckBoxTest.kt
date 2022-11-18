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

class CheckBoxTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun checkbox_should_work_correctly() {
    // given
    var checked by mutableStateOf(false)

    composeTestRule.setContent {
      TwineTheme {
        CheckBox(
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
  fun when_label_is_null_then_hide_label() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        CheckBox(
          checked = false,
          label = null,
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("CheckBox:Label", useUnmergedTree = true)
      .assertDoesNotExist()
  }

  @Test
  fun when_label_is_not_null_then_show_label() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        CheckBox(
          checked = false,
          label = "Enable?",
          onValueChange = {
            // Handle value changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("CheckBox:Label", useUnmergedTree = true)
      .assertExists()
  }

  @Test
  fun disabling_checkbox_should_work_correctly() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        CheckBox(
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
      .onNodeWithTag("CheckBox")
      .assertIsNotEnabled()
  }

  @Test
  fun enabling_checkbox_should_work_correctly() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        CheckBox(
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
      .onNodeWithTag("CheckBox")
      .assertIsEnabled()
      .assertHasClickAction()
  }
}
