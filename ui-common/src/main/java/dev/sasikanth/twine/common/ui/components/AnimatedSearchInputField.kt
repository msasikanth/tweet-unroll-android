package dev.sasikanth.twine.common.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.theme.TwineTheme

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun AnimatedSearchInputField(
  modifier: Modifier = Modifier,
  isExpanded: Boolean = false,
  text: String,
  onClearTextClick: () -> Unit,
  onValueChanged: (String) -> Unit
) {
  BoxWithConstraints(
    modifier = modifier.height(56.dp),
    contentAlignment = Alignment.CenterEnd
  ) {
    val focusRequester = remember { FocusRequester() }
    val transition = updateTransition(targetState = isExpanded, label = "")

    val backgroundSize by transition.animateDp(
      label = "SearchInput:Expanded",
      transitionSpec = {
        spring(stiffness = Spring.StiffnessMediumLow)
      }
    ) { isExpanded ->
      if (isExpanded) {
        this.maxWidth
      } else {
        40.dp
      }
    }

    val cornerRadius by transition.animateDp(
      label = "SearchInput:CornerRadius",
      transitionSpec = {
        spring(stiffness = Spring.StiffnessMediumLow)
      }
    ) { isExpanded ->
      if (isExpanded)
        16.dp
      else
        20.dp
    }

    if (transition.isRunning && !isExpanded) {
      LocalFocusManager.current.clearFocus()
    }

    SideEffect {
      if (!transition.isRunning && isExpanded) {
        focusRequester.requestFocus()
      }
    }

    InputField(
      modifier = Modifier
        .size(backgroundSize)
        .clip(RoundedCornerShape(cornerRadius)),
      text = text,
      hint = "Search",
      focusRequester = focusRequester,
      startSlot = {
        IconButton(
          onClick = { },
          contentColor = TwineTheme.colorScheme.onPrimaryContainer,
          enabled = transition.targetState
        ) {
          val animatedImageVector = AnimatedImageVector
            .animatedVectorResource(id = R.drawable.avd_search_to_close)

          Icon(
            painter = rememberAnimatedVectorPainter(
              animatedImageVector = animatedImageVector,
              atEnd = isExpanded
            ),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
          )
        }
      },
      endSlot = {
        androidx.compose.animation.AnimatedVisibility(
          visible = isExpanded,
          enter = fadeIn(
            animationSpec = spring(stiffness = Spring.StiffnessMedium)
          ),
          exit = fadeOut(
            animationSpec = spring(stiffness = Spring.StiffnessHigh)
          )
        ) {
          IconButton(
            onClick = { },
            backgroundColor = TwineTheme.colorScheme.brand,
            enabled = transition.targetState
          ) {
            Icon(
              imageVector = Icons.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
          }
        }
      },
      onValueChange = onValueChanged,
      onClearTextClick = onClearTextClick
    )
  }
}
