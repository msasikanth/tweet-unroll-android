package dev.sasikanth.twine.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Story
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Timeline
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class ViewModeToggleTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Composable
  private fun setUi(state: MutableState<ViewModeToggle>) {
    TwineTheme {
      ViewModeToggle(
        viewModeToggle = state.value,
        onViewModeChanged = { viewModeToggle ->
          state.value = viewModeToggle
        }
      )
    }
  }

  @Test
  fun switching_view_mode_from_timeline_to_story_should_work_correctly_on_click() {
    // given
    val viewModeToggleState = mutableStateOf(Timeline)

    composeTestRule.setContent {
      setUi(state = viewModeToggleState)
    }

    // when
    composeTestRule
      .onNodeWithTag("ViewMode:Story")
      .performClick()

    // then
    assertThat(viewModeToggleState.value).isEqualTo(Story)
  }

  @Test
  fun switching_view_mode_from_story_to_timeline_should_work_correctly_on_click() {
    // given
    val viewModeToggleState = mutableStateOf(Story)

    composeTestRule.setContent {
      setUi(state = viewModeToggleState)
    }


    // when
    composeTestRule
      .onNodeWithTag("ViewMode:Timeline")
      .performClick()

    // then
    assertThat(viewModeToggleState.value).isEqualTo(Timeline)
  }

  @Test
  fun switching_view_mode_from_timeline_to_story_should_work_correctly_on_gesture() {
    // given
    val viewModeToggleState = mutableStateOf(Timeline)

    composeTestRule.setContent {
      setUi(state = viewModeToggleState)
    }

    // when
    composeTestRule
      .onNodeWithTag("ViewMode:SelectorLayout")
      .performTouchInput {
        swipeRight()
      }

    // then
    composeTestRule.runOnIdle {
      assertThat(viewModeToggleState.value).isEqualTo(Story)
    }
  }

  @Test
  fun switching_view_mode_from_story_to_timeline_should_work_correctly_on_gesture() {
    // given
    val viewModeToggleState = mutableStateOf(Story)

    composeTestRule.setContent {
      setUi(state = viewModeToggleState)
    }

    // when
    composeTestRule
      .onNodeWithTag("ViewMode:SelectorLayout")
      .performTouchInput {
        swipeLeft()
      }

    // then
    composeTestRule.runOnIdle {
      assertThat(viewModeToggleState.value).isEqualTo(Timeline)
    }
  }
}
