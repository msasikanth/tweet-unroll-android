package dev.sasikanth.twine.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.components.InputField
import dev.sasikanth.twine.common.ui.components.SubHeader
import dev.sasikanth.twine.common.ui.components.TwineButton
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@Composable
internal fun HeroInput(
  modifier: Modifier = Modifier,
  text: String,
  onPasteClick: () -> Unit,
  onTextChange: (String) -> Unit,
) {
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

    InputField(
      modifier = Modifier.fillMaxWidth(),
      text = text,
      hint = stringResource(id = R.string.home_hero_input_hint),
      onValueChange = onTextChange,
      endSlot = {
        TwineButton(
          text = stringResource(id = R.string.home_hero_input_action),
          onClick = onPasteClick
        )
      }
    )

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp),
      text = stringResource(id = R.string.home_hero_input_tooltip),
      style = TwineTheme.typography.bodySmall,
      color = TwineTheme.colorScheme.onSurfaceVariant
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
        onPasteClick = {}
      ) {}
    }
  }
}
