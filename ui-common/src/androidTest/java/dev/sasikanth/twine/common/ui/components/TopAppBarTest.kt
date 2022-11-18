package dev.sasikanth.twine.common.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun when_app_bar_has_no_navigation_icon_and_action_buttons_then_show_default_top_app_bar() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        TopAppBar(
          title = "Twine"
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("TopAppBar:NavigationIcon")
      .assertDoesNotExist()

    composeTestRule
      .onNodeWithTag("TopAppBar:Actions")
      .assertDoesNotExist()
  }

  @Test
  fun when_app_bar_has_navigation_icon_then_show_navigation_icon() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        TopAppBar(
          title = "Twine",
          navigationIcon = {
            AppBarBackButton {
              // Handle back button clicks
            }
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("TopAppBar:NavigationIcon")
      .assertExists()

    composeTestRule
      .onNodeWithTag("TopAppBar:Actions")
      .assertDoesNotExist()
  }

  @Test
  fun when_app_bar_has_actions_then_show_actions() {
    // given
    composeTestRule.setContent {
      TwineTheme {
        TopAppBar(
          title = "Twine",
          actions = {
            AppBarActionButton(icon = R.drawable.ic_alert_off) {
              // Handle clicks
            }
          }
        )
      }
    }

    // then
    composeTestRule
      .onNodeWithTag("TopAppBar:NavigationIcon")
      .assertDoesNotExist()

    composeTestRule
      .onNodeWithTag("TopAppBar:Actions")
      .assertExists()
  }
}
