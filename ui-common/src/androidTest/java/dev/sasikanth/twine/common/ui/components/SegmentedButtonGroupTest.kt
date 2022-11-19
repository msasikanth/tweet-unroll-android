package dev.sasikanth.twine.common.ui.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class SegmentedButtonGroupTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun number_of_buttons_in_segmented_button_group_should_be_displayed_based_on_items() {
    // given
    val light = SegmentedButtonItem(label = "Light")
    val dark = SegmentedButtonItem(label = "Dark")
    val items = listOf(light, dark)

    composeTestRule.setContent {
      TwineTheme {
        SegmentedButtonGroup(
          items = items,
          selectedItem = dark,
          onItemChange = {
            // Handle item changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("SegmentedButtonGroup")
      .onChildren()
      .assertCountEquals(2)
  }

  @Test
  fun selected_item_in_the_button_group_should_be_toggled() {
    // given
    val light = SegmentedButtonItem(label = "Light")
    val dark = SegmentedButtonItem(label = "Dark")
    val items = listOf(light, dark)

    composeTestRule.setContent {
      TwineTheme {
        SegmentedButtonGroup(
          items = items,
          selectedItem = dark,
          onItemChange = {
            // Handle item changes
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("SegmentedButtonGroup")
      .onChildAt(0)
      .assertIsOff()

    composeTestRule
      .onNodeWithTag("SegmentedButtonGroup")
      .onChildAt(1)
      .assertIsOn()
  }
}
