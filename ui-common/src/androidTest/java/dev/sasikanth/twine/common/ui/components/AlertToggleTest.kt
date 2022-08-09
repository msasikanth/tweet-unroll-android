package dev.sasikanth.twine.common.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import org.junit.Rule
import org.junit.Test

class AlertToggleTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Composable
  private fun setUi(state: MutableState<Boolean>) {
    TwineTheme {
      AlertToggle(
        isToggled = state.value,
        onAlertToggled = { isToggled ->
          state.value = isToggled
        }
      )
    }
  }

  @Test
  fun clicking_alert_toggle_should_turn_it_on_if_its_off() {
    // given
    val alertToggleState = mutableStateOf(false)
    composeTestRule.setContent {
      setUi(state = alertToggleState)
    }

    // when
    composeTestRule
      .onRoot()
      .performClick()

    // then
    assertThat(alertToggleState.value).isEqualTo(true)
  }

  @Test
  fun clicking_alert_toggle_should_turn_it_off_if_its_on() {
    // given
    val alertToggleState = mutableStateOf(true)
    composeTestRule.setContent {
      setUi(state = alertToggleState)
    }

    // when
    composeTestRule
      .onRoot()
      .performClick()

    // then
    assertThat(alertToggleState.value).isEqualTo(false)
  }
}
