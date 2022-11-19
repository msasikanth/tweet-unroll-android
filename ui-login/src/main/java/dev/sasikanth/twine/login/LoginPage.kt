package dev.sasikanth.twine.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.twine.common.ui.textures.AnimatedStripeBackground
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoginPage(
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel(),
  navigateToHome: () -> Unit
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
      contract = viewModel.buildTwineLoginActivityResult()!!,
      onResult = viewModel::onLoginResult
    )

    if (uiState.isUserLoggedIn) {
      LaunchedEffect(Unit) {
        navigateToHome()
      }
    }

    if (!uiState.isCheckingLoginStatus && !uiState.isUserLoggedIn) {
      LoginPageContent {
        launcher.launch(Unit)
      }
    }
  }
}

@Composable
private fun LoginPageContent(
  modifier: Modifier = Modifier,
  onLoginClick: () -> Unit
) {
  AnimatedStripeBackground(
    modifier = Modifier.fillMaxSize(),
    animationSpec = infiniteRepeatable(
      tween(
        1000,
        easing = LinearEasing
      )
    )
  ) {
    Column(
      modifier = modifier
        .systemBarsPadding()
        .fillMaxSize()
        .padding(40.dp),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Greeting()
      SignInButtonContainer(onLoginClick = onLoginClick)
    }
  }
}

@Preview(
  showBackground = true
)
@Composable
private fun LoginPagePreview() {
  TwineTheme {
    Box {
      LoginPageContent {
        // Handle login button clicks
      }
    }
  }
}
