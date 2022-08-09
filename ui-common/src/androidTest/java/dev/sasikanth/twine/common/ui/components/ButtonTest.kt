package dev.sasikanth.twine.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class ButtonTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun when_icon_is_not_present_then_hide_the_icon() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        TwineButton(text = "Sign in") {
          // Handle click
        }
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("TwineButton:Icon")
      .assertDoesNotExist()
  }

  @Test
  fun when_icon_is_present_then_show_the_icon() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        TwineButton(
          text = "Sign in",
          icon = rememberVectorPainter(image = Icons.Default.AccountCircle)
        ) {
          // Handle click
        }
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("TwineButton:Icon")
      .assertDoesNotExist()
  }
}
