package dev.sasikanth.twine.common.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.sasikanth.twine.common.ui.components.IconButtonDefaults.IconSize
import dev.sasikanth.twine.common.ui.components.IconButtonDefaults.IconSizeSmall
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class IconButtonTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun when_icon_button_background_is_unfilled_then_icon_size_should_be_default_icon_size() {
    // given
    composeTestRule.setContent {
      IconButton(
        onClick = {
          // Handle clicks
        },
        backgroundColor = Color.Unspecified
      ) {
        Icon(
          imageVector = Icons.Filled.ArrowBack,
          contentDescription = null
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Icon", useUnmergedTree = true)
      .assertWidthIsEqualTo(IconSize)
      .assertHeightIsEqualTo(IconSize)
  }

  @Test
  fun when_icon_button_background_is_unfilled_then_icon_size_should_be_small() {
    // given
    composeTestRule.setContent {
      IconButton(
        onClick = {
          // Handle clicks
        },
        backgroundColor = TwineTheme.colorScheme.brand
      ) {
        Icon(
          imageVector = Icons.Filled.ArrowBack,
          contentDescription = null
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("Icon", useUnmergedTree = true)
      .assertWidthIsEqualTo(IconSizeSmall)
      .assertHeightIsEqualTo(IconSizeSmall)
  }
}
