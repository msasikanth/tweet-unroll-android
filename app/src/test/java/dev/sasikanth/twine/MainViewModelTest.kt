package dev.sasikanth.twine

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.sasikanth.twine.common.testing.preferences.FakeAppPreferences
import dev.sasikanth.twine.data.prefrences.Theme.DARK
import dev.sasikanth.twine.data.prefrences.Theme.SYSTEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

  private lateinit var viewModel: MainViewModel

  private val appPreferences = FakeAppPreferences()
  private val uiState = MainUiState.DEFAULT

  @Before
  fun setup() {
    val testDispatcher = UnconfinedTestDispatcher()
    Dispatchers.setMain(testDispatcher)

    viewModel = MainViewModel(
      appPreferences = appPreferences
    )
  }

  @Test
  fun `when viewmodel is created, then listen to current theme changes`() = runTest {
    // given
    assertThat(viewModel.uiState.value.theme).isEqualTo(SYSTEM)

    // when
    viewModel.setTheme(DARK)

    // then
    viewModel.uiState.test {
      assertThat(awaitItem()).isEqualTo(uiState.copy(theme = DARK))
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `when viewmodel is created, then listen to dynamic color support changes`() = runTest {
    // given
    assertThat(viewModel.uiState.value.useDynamicColors).isTrue()

    // when
    viewModel.setDynamicColors(false)

    // then
    viewModel.uiState.test {
      assertThat(awaitItem()).isEqualTo(uiState.copy(useDynamicColors = false))
      cancelAndIgnoreRemainingEvents()
    }
  }
}
