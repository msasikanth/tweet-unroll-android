package dev.sasikanth.twine.common.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.anim.TwineSpring
import dev.sasikanth.twine.common.ui.components.InputFieldDefaults.InputFieldMinHeight
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

/**
 * Component that lets user edit text using either software or hardware keyboard.
 *
 * Whenever the user edits the text, onValueChange is called with the most up to date state
 * represented by [String] with which developer is expected to update their state.
 *
 * @param text: input text to be shown in the text field.
 * @param hint: hint text to be shown in the text field, when input text is empty.
 * @param singleLine: when set to true, this text field becomes a single horizontally scrolling
 * text field instead of wrapping onto multiple lines. The keyboard will be informed to not show
 * the return key as the [ImeAction].
 * @param keyboardOptions: software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction].
 * @param keyboardActions: when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction].
 * @param startSlot: composable to be shown at the start of the text field. Usually an
 * [IconButton], [TwineButton].
 * @param endSlot: composable to be shown at the end of the text field. Usually an
 * [IconButton], [TwineButton].
 * @param onValueChange: callback to notify the state when the user enters text
 */
@Composable
fun InputField(
  modifier: Modifier = Modifier,
  text: String,
  hint: String,
  singleLine: Boolean = true,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  startSlot: @Composable (BoxScope.() -> Unit)? = null,
  endSlot: @Composable (BoxScope.() -> Unit)? = null,
  onValueChange: (String) -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isFocused by interactionSource.collectIsFocusedAsState()

  val transition = updateTransition(
    targetState = isFocused,
    label = "InputField:FocusChanges"
  )
  val animationSpec = spring<Color>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = TwineSpring.StiffnessMedium
  )

  val backgroundColor by transition.animateColor(
    label = "InputField:BackgroundColor",
    transitionSpec = { animationSpec }
  ) {
    if (it) {
      TwineTheme
        .colorScheme
        .surfaceColorAtElevation(ElevationTokens.Level5)
    } else {
      TwineTheme
        .colorScheme
        .surfaceColorAtElevation(ElevationTokens.Level2)
    }
  }

  val textColor by transition.animateColor(
    label = "InputField:TextColor",
    transitionSpec = { animationSpec }
  ) {
    if (it) {
      TwineTheme.colorScheme.onSurface
    } else if (text.isNotBlank()) {
      TwineTheme.colorScheme.onSurfaceVariant
    } else {
      TwineTheme.colorScheme.outline
    }
  }

  val slotsPaddingModifier = Modifier.padding(horizontal = 4.dp)

  Row(
    modifier = modifier
      .defaultMinSize(minHeight = InputFieldMinHeight)
      .clip(TwineTheme.shapes.large)
      .background(backgroundColor),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (startSlot == null) {
      Spacer(modifier = Modifier.width(16.dp))
    } else {
      Box(
        modifier = slotsPaddingModifier.testTag("InputField:StartSlot"),
        content = startSlot
      )
    }

    BasicTextField(
      modifier = Modifier
        .weight(1f)
        .testTag("InputField:Text"),
      value = text,
      onValueChange = onValueChange,
      interactionSource = interactionSource,
      textStyle = TwineTheme.typography.bodyLarge.copy(color = textColor),
      decorationBox = { innerTextField ->
        if (text.isBlank()) {
          Text(
            modifier = Modifier.testTag("InputField:Hint"),
            text = hint,
            style = TwineTheme.typography.bodyLarge,
            color = TwineTheme.colorScheme.outline
          )
        }

        innerTextField.invoke()
      },
      singleLine = singleLine,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      cursorBrush = SolidColor(TwineTheme.colorScheme.onSurface)
    )

    if (endSlot == null) {
      Spacer(modifier = Modifier.width(16.dp))
    } else {
      Box(
        modifier = slotsPaddingModifier.testTag("InputField:EndSlot"),
        content = endSlot
      )
    }
  }
}

internal object InputFieldDefaults {
  val InputFieldMinHeight = 56.dp
}

@Preview
@Composable
private fun InputFieldPreview_EndSlot() {
  TwineTheme {
    InputField(
      text = "",
      hint = "Label",
      endSlot = {
        TwineButton(text = "Action") {

        }
      },
    ) {
      // Handle text changes
    }
  }
}

@Preview
@Composable
private fun InputFieldPreview_StartSlot_EndSlot() {
  TwineTheme {
    InputField(
      text = "",
      hint = "Label",
      startSlot = {
        IconButton(onClick = { }) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
          )
        }
      },
      endSlot = {
        IconButton(
          onClick = { },
          backgroundColor = TwineTheme.colorScheme.brand
        ) {
          Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null
          )
        }
      },
    ) {
      // Handle text changes
    }
  }
}
