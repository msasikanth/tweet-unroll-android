package dev.sasikanth.twine.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.IconButton
import dev.sasikanth.twine.common.ui.components.InputField
import dev.sasikanth.twine.common.ui.components.SubHeader
import dev.sasikanth.twine.common.ui.components.TwineButton
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@Composable
internal fun HeroInput(
  modifier: Modifier = Modifier,
  text: String,
  inputErrors: List<InputError> = emptyList(),
  onPasteClick: () -> Unit,
  onGoClick: () -> Unit,
  onTextChange: (String) -> Unit,
  onClearTextClick: () -> Unit
) {
  val hasInputErrors = inputErrors.any {
    it == InvalidUrl
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        start = 24.dp,
        end = 24.dp,
        bottom = 16.dp
      )
  ) {
    SubHeader(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.home_hero_input_heading)
    )

    val keyboardActions = if (text.isNotBlank()) {
      KeyboardActions(
        onGo = { onGoClick.invoke() }
      )
    } else {
      KeyboardActions()
    }

    InputField(
      modifier = Modifier.fillMaxWidth(),
      text = text,
      hint = stringResource(id = R.string.home_hero_input_hint),
      onValueChange = onTextChange,
      endSlot = {
        InputFieldEndSlot(
          hasText = text.isNotBlank(),
          hasInputErrors = hasInputErrors,
          onPasteClick = onPasteClick,
          onGoClick = onGoClick
        )
      },
      onClearTextClick = onClearTextClick,
      keyboardActions = keyboardActions,
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Go,
        keyboardType = KeyboardType.Uri
      )
    )

    Tooltip(hasInputErrors = hasInputErrors)
  }
}

@Composable
private fun Tooltip(
  modifier: Modifier = Modifier,
  hasInputErrors: Boolean
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .padding(top = 8.dp)
  ) {
    val tooltipAlpha = if (hasInputErrors) 0f else 1f

    Text(
      modifier = Modifier
        .alpha(tooltipAlpha),
      text = stringResource(id = R.string.home_hero_input_tooltip),
      style = TwineTheme.typography.bodySmall,
      color = TwineTheme.colorScheme.onSurfaceVariant
    )

    if (hasInputErrors) {
      ErrorLabel()
    }
  }
}

@Composable
private fun ErrorLabel(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      modifier = Modifier.size(16.dp),
      imageVector = Icons.Filled.ErrorOutline,
      contentDescription = null,
      tint = TwineTheme.colorScheme.error
    )

    Spacer(modifier = Modifier.width(4.dp))

    Text(
      text = stringResource(id = R.string.home_hero_input_invalid_url),
      style = TwineTheme.typography.labelMedium,
      color = TwineTheme.colorScheme.error
    )
  }
}

@Composable
private fun InputFieldEndSlot(
  hasText: Boolean,
  hasInputErrors: Boolean,
  onPasteClick: () -> Unit,
  onGoClick: () -> Unit
) {
  if (hasText) {
    IconButton(
      backgroundColor = TwineTheme.colorScheme.brand,
      enabled = !hasInputErrors,
      content = {
        Icon(
          imageVector = Icons.Filled.ArrowForward,
          contentDescription = null
        )
      },
      onClick = onGoClick
    )
  } else {
    TwineButton(
      text = stringResource(id = R.string.home_hero_input_action),
      onClick = onPasteClick
    )
  }
}

@Preview
@Composable
private fun HeroInputPreview() {
  TwineTheme {
    Surface {
      HeroInput(
        text = "",
        onPasteClick = {},
        onTextChange = {},
        onClearTextClick = {},
        onGoClick = {}
      )
    }
  }
}

@Preview
@Composable
private fun HeroInputWithTextPreview() {
  TwineTheme {
    Surface {
      HeroInput(
        text = "https://twitter.com/",
        onPasteClick = {},
        onTextChange = {},
        onClearTextClick = {},
        onGoClick = {}
      )
    }
  }
}

@Preview
@Composable
private fun HeroInputWithInvalidTextPreview() {
  TwineTheme {
    Surface {
      HeroInput(
        text = "https://twitter.com/",
        inputErrors = listOf(InvalidUrl),
        onPasteClick = {},
        onTextChange = {},
        onClearTextClick = {},
        onGoClick = {}
      )
    }
  }
}
