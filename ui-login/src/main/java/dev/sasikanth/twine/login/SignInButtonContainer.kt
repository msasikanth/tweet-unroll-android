package dev.sasikanth.twine.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@Composable
internal fun SignInButtonContainer(
  modifier: Modifier = Modifier,
  onLoginClick: () -> Unit,
) {
  Column(
    modifier = modifier.wrapContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Text(
      text = stringResource(id = R.string.login_ready_to_get_started),
      style = TwineTheme.typography.headlineSmall,
      color = TwineTheme.colorScheme.onSurfaceVariant,
    )

    SignInButton(onClick = onLoginClick)
  }
}

@Composable
private fun SignInButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  ExtendedFloatingActionButton(
    modifier = modifier,
    onClick = onClick,
    containerColor = TwineTheme.colorScheme.brand,
    contentColor = TwineTheme.colorScheme.onBrand,
    elevation = FloatingActionButtonDefaults.elevation(
      defaultElevation = 0.dp,
      pressedElevation = 0.dp,
      focusedElevation = 0.dp,
      hoveredElevation = 0.dp
    ),
  ) {
    Text(
      text = stringResource(id = R.string.login_sign_in_with_twitter),
      style = TwineTheme.typography.labelLarge,
    )
  }
}

@Preview(
  showBackground = true,
)
@Composable
fun SignInButtonContainerPreview() {
  TwineTheme {
    SignInButtonContainer(
      modifier = Modifier.padding(16.dp)
    ) {
      // Handle sign in button click
    }
  }
}

