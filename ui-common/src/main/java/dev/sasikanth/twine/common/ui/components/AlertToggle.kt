package dev.sasikanth.twine.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.anim.TwineSpring
import dev.sasikanth.twine.common.ui.components.AlertToggleDefaults.InnerContainerSize
import dev.sasikanth.twine.common.ui.components.AlertToggleDefaults.OuterContainerSize
import dev.sasikanth.twine.common.ui.components.AlertToggleDefaults.ToggledContainerCornerRadius
import dev.sasikanth.twine.common.ui.components.AlertToggleDefaults.UnToggledContainerCornerRadius
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation

private object AlertToggleDefaults {
  val ToggledContainerCornerRadius = 20.dp
  val UnToggledContainerCornerRadius = 16.dp

  val OuterContainerSize = 64.dp
  val InnerContainerSize = 56.dp
}

/**
 * Icon button that is toggleable.
 *
 * Indicates whether alerts are enabled or not
 *
 * @param isToggled: indicates whether alerts are on or off
 * @param onAlertToggled: callback to be invoked with new state when button is clicked
 * to request state change
 */
@Composable
fun AlertToggle(
  modifier: Modifier = Modifier,
  isToggled: Boolean,
  onAlertToggled: (Boolean) -> Unit
) {
  val rippleColor = if (isToggled) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.primary
  }

  Box(
    modifier = modifier.size(OuterContainerSize),
    contentAlignment = Alignment.Center
  ) {
    val containerColor = if (isToggled) {
      TwineTheme.colorScheme.surfaceColorAtElevation(ElevationTokens.Level5)
    } else {
      TwineTheme
        .colorScheme
        .primary
        .copy(
          alpha = TwineTheme.opacity.hovered
        )
    }

    val transition = updateTransition(targetState = isToggled, label = "AlertToggle")
    val backgroundContainerSize by transition
      .animateDp(label = "AlertToggle:BackgroundContainer") {
        if (it) OuterContainerSize else InnerContainerSize
      }

    val cornerRadius by transition
      .animateDp(label = "AlertToggled:CornerRadius") {
        if (it) ToggledContainerCornerRadius else UnToggledContainerCornerRadius
      }

    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(cornerRadius))
        .toggleable(
          value = isToggled,
          onValueChange = onAlertToggled,
          interactionSource = remember { MutableInteractionSource() },
          indication = rememberRipple(color = rippleColor)
        )
        .size(backgroundContainerSize)
        .background(color = containerColor),
      contentAlignment = Alignment.Center
    ) {
      Content(isToggled = isToggled)
    }
  }
}

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalAnimationApi::class)
@Composable
private fun Content(
  modifier: Modifier = Modifier,
  isToggled: Boolean
) {
  val onContentColor = if (isToggled) {
    TwineTheme.colorScheme.onBrand
  } else {
    TwineTheme.colorScheme.onPrimaryContainer
  }

  val contentDescription = if (isToggled) {
    R.string.cd_alert_toggle_on
  } else {
    R.string.cd_alert_toggle_off
  }

  val alertToggleIcon = AnimatedImageVector
    .animatedVectorResource(id = R.drawable.avd_alert_toggle)

  Box(
    modifier = modifier.size(InnerContainerSize),
    contentAlignment = Alignment.Center,
  ) {
    AnimatedVisibility(
      visible = isToggled,
      enter = scaleIn(
        animationSpec = spring(
          dampingRatio = TwineSpring.DampingRatioHigh,
          stiffness = TwineSpring.StiffnessMedium
        ),
        initialScale = 0.5f
      ) + fadeIn(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioNoBouncy,
          stiffness = TwineSpring.StiffnessMedium
        ),
      ),
      exit = scaleOut(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioNoBouncy,
          stiffness = TwineSpring.StiffnessMedium
        ),
        targetScale = 0.5f
      ) + fadeOut(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioNoBouncy,
          stiffness = Spring.StiffnessMedium
        )
      ),
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            color = TwineTheme.colorScheme.brand,
            shape = TwineTheme.shapes.large
          )
      )
    }

    Icon(
      painter = rememberAnimatedVectorPainter(
        animatedImageVector = alertToggleIcon,
        atEnd = isToggled
      ),
      contentDescription = stringResource(id = contentDescription),
      tint = onContentColor
    )
  }
}

@Preview
@Composable
private fun AlertTogglePreview_isToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = true
    ) {

    }
  }
}

@Preview
@Composable
private fun AlertTogglePreview_isUnToggled() {
  TwineTheme {
    AlertToggle(
      isToggled = false
    ) {

    }
  }
}
