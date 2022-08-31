package dev.sasikanth.twine.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class InputFieldTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun when_text_is_empty_then_hint_should_be_shown() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = "",
          hint = "Label",
          onValueChange = {
            // Handle text changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("InputField:Hint", useUnmergedTree = true)
      .assertExists()

    composeTestRule
      .onNodeWithTag("InputField:Hint", useUnmergedTree = true)
      .assertTextEquals("Label")
  }

  @Test
  fun when_text_is_not_empty_then_hint_should_not_be_shown() {
    // given
    val text = mutableStateOf("")

    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = text.value,
          hint = "Label",
          onValueChange = {
            text.value = it
          }
        )
      }
    }

    composeTestRule
      .onNodeWithTag("InputField:Hint", useUnmergedTree = true)
      .assertExists()

    // when
    composeTestRule
      .onNodeWithTag("InputField:Text")
      .performTextInput("Entering text")

    // then
    composeTestRule
      .onNodeWithTag("InputField:Hint", useUnmergedTree = true)
      .assertDoesNotExist()
  }

  @Test
  fun text_changes_in_input_field_should_work_correctly() {
    // given
    val text = mutableStateOf("")

    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = text.value,
          hint = "Label",
          onValueChange = {
            text.value = it
          }
        )
      }
    }

    // when
    composeTestRule
      .onNodeWithTag("InputField:Text")
      .performTextInput("Entering text")

    // then
    composeTestRule
      .onNodeWithTag("InputField:Text")
      .assertTextEquals("Entering text")
  }

  @Test
  fun when_start_slot_is_present_then_display_start_slot() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = "",
          hint = "Label",
          startSlot = {
            IconButton(onClick = { }) {
              Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
          },
          onValueChange = {
            // Handle text change
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("InputField:StartSlot")
      .assertExists()
  }

  @Test
  fun when_start_slot_is_not_present_then_hide_start_slot() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = "",
          hint = "Label",
          startSlot = null,
          onValueChange = {
            // Handle text change
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("InputField:StartSlot")
      .assertDoesNotExist()
  }

  @Test
  fun when_end_slot_is_present_then_display_end_slot() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = "",
          hint = "Label",
          endSlot = {
            IconButton(onClick = { }) {
              Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
          },
          onValueChange = {
            // Handle text change
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("InputField:EndSlot")
      .assertExists()
  }

  @Test
  fun when_end_slot_is_not_present_then_hide_end_slot() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        InputField(
          text = "",
          hint = "Label",
          endSlot = null,
          onValueChange = {
            // Handle text change
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("InputField:EndSlot")
      .assertDoesNotExist()
  }
}
