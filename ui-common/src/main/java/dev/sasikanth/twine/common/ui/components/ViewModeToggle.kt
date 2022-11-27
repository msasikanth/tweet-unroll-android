package dev.sasikanth.twine.common.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import dev.sasikanth.twine.common.ui.R
import dev.sasikanth.twine.common.ui.anim.TwineSpring
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Story
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Timeline
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlin.math.roundToInt

private object ViewModeToggleDefaults {
  val selectorCornerRadius = 16.dp
  val containerCornerRadius = 20.dp
  val containerPadding = 4.dp
  val buttonRowSpacing = 4.dp
  val iconPadding = 16.dp
}

enum class ViewModeToggle {
  Timeline,
  Story
}

/**
 * Segmented toggleable buttons for switching between different view modes of Twine
 *
 * @param viewModeToggle: indicates [ViewModeToggle] state
 * @param onViewModeChanged: callback invoked with [ViewModeToggle] when buttons are clicked
 * or swiped to
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewModeToggle(
  viewModeToggle: ViewModeToggle,
  modifier: Modifier = Modifier,
  onViewModeChanged: (ViewModeToggle) -> Unit
) {
  var size by remember { mutableStateOf(Size.Zero) }

  // Since `rememberSwipeableStateFor` is not public from Material package.
  // We are adding it's functionality in this composable.
  val swipeableState = rememberSwipeableState(
    initialValue = viewModeToggle,
    confirmStateChange = { true }
  )
  val forceAnimationCheck = remember { mutableStateOf(false) }

  LaunchedEffect(viewModeToggle, forceAnimationCheck.value) {
    if (viewModeToggle != swipeableState.currentValue) {
      swipeableState.animateTo(
        targetValue = viewModeToggle,
        anim = spring(
          dampingRatio = TwineSpring.DampingRatioHigh,
          stiffness = TwineSpring.StiffnessMedium
        )
      )
    }
  }

  DisposableEffect(swipeableState.currentValue) {
    if (viewModeToggle != swipeableState.currentValue) {
      onViewModeChanged(swipeableState.currentValue)
      forceAnimationCheck.value = !forceAnimationCheck.value
    }
    onDispose { }
  }

  val buttonSpacingInPx =
    with(LocalDensity.current) { ViewModeToggleDefaults.buttonRowSpacing.roundToPx() }
  val selectedButtonWidth = if (size.width == 0f) {
    // temp value until we get the actual size
    1f
  } else {
    (size.width + buttonSpacingInPx) / 2
  }

  val anchors = persistentMapOf(
    0f to Timeline,
    selectedButtonWidth to Story
  )

  // Applying a tint color on our surface, without any actual elevation
  val surfaceColor = TwineTheme
    .colorScheme
    .surfaceColorAtElevation(ElevationTokens.Level5)

  Box(
    modifier
      .wrapContentWidth()
      .wrapContentHeight()
      .background(
        color = surfaceColor,
        shape = RoundedCornerShape(ViewModeToggleDefaults.containerCornerRadius)
      )
      .padding(ViewModeToggleDefaults.containerPadding)
  ) {
    IconButtonRow(
      isAnimationRunning = swipeableState.isAnimationRunning,
      onViewModeChanged = {
        if (!swipeableState.isAnimationRunning) {
          onViewModeChanged(it)
        }
      }
    )

    SelectorLayout(
      modifier = Modifier
        .onSizeChanged { size = it.toSize() }
        .testTag("ViewMode:SelectorLayout"),
      swipeableState = swipeableState,
      anchors = anchors
    )
  }
}

/**
 * Clipping layer to indicate selected button and also helps us provide a clipping animation
 * when overlapping different colored buttons in the group.
 *
 * see also: [SelectorShape]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SelectorLayout(
  swipeableState: SwipeableState<ViewModeToggle>,
  anchors: ImmutableMap<Float, ViewModeToggle>,
  modifier: Modifier = Modifier
) {
  val offset = IntOffset(
    x = swipeableState.offset.value.roundToInt(),
    y = 0
  ).toOffset()
  Row(
    modifier = modifier
      .clip(shape = SelectorShape(offset))
      .background(TwineTheme.colorScheme.brand)
      .swipeable(
        state = swipeableState,
        anchors = anchors,
        thresholds = { _, _ -> FractionalThreshold(0.3f) },
        orientation = Orientation.Horizontal,
        resistance = SwipeableDefaults.resistanceConfig(
          anchors = anchors.keys,
          factorAtMin = 0f,
          factorAtMax = 0f
        )
      ),
    horizontalArrangement = Arrangement.spacedBy(ViewModeToggleDefaults.buttonRowSpacing)
  ) {
    // We are consume clicks here, so that we don't show ripples when swiping
    TimelineIcon(
      modifier = Modifier.clickable(enabled = false) {},
      iconTint = TwineTheme.colorScheme.onBrand
    )
    StoryIcon(
      modifier = Modifier.clickable(enabled = false) {},
      iconTint = TwineTheme.colorScheme.onBrand
    )
  }
}

@Composable
private fun IconButtonRow(
  isAnimationRunning: Boolean,
  modifier: Modifier = Modifier,
  onViewModeChanged: (ViewModeToggle) -> Unit
) {
  val iconTint = if (isAnimationRunning) {
    TwineTheme.colorScheme.outline
  } else {
    TwineTheme.colorScheme.onPrimaryContainer
  }

  Row(
    modifier = modifier
      .clip(TwineTheme.shapes.large),
    horizontalArrangement = Arrangement.spacedBy(ViewModeToggleDefaults.buttonRowSpacing)
  ) {
    IconButton(
      modifier = Modifier
        .testTag("ViewMode:Timeline"),
      resource = R.drawable.ic_view_mode_timeline,
      iconTint = iconTint,
      enabled = !isAnimationRunning,
      contentDescription = stringResource(id = R.string.cd_view_mode_timeline)
    ) {
      onViewModeChanged(Timeline)
    }

    IconButton(
      modifier = Modifier
        .testTag("ViewMode:Story"),
      resource = R.drawable.ic_view_mode_story,
      iconTint = iconTint,
      enabled = !isAnimationRunning,
      contentDescription = stringResource(id = R.string.cd_view_mode_story)
    ) {
      onViewModeChanged(Story)
    }
  }
}

@Composable
private fun IconButton(
  @DrawableRes resource: Int,
  iconTint: Color,
  modifier: Modifier = Modifier,
  contentDescription: String? = null,
  enabled: Boolean = true,
  onClick: () -> Unit
) {
  Icon(
    modifier = modifier
      .clickable(
        onClick = onClick,
        role = Role.Button,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
          bounded = false,
          radius = 100.dp
        ),
        enabled = enabled
      )
      .padding(ViewModeToggleDefaults.iconPadding),
    painter = painterResource(id = resource),
    contentDescription = contentDescription,
    tint = iconTint
  )
}

@Composable
private fun TimelineIcon(
  iconTint: Color,
  modifier: Modifier = Modifier
) {
  Icon(
    modifier = modifier.padding(ViewModeToggleDefaults.iconPadding),
    painter = painterResource(id = R.drawable.ic_view_mode_timeline),
    contentDescription = stringResource(id = R.string.cd_view_mode_timeline),
    tint = iconTint
  )
}

@Composable
private fun StoryIcon(
  iconTint: Color,
  modifier: Modifier = Modifier
) {
  Icon(
    modifier = modifier.padding(ViewModeToggleDefaults.iconPadding),
    painter = painterResource(id = R.drawable.ic_view_mode_story),
    contentDescription = stringResource(id = R.string.cd_view_mode_story),
    tint = iconTint
  )
}

@Preview(group = "ViewModeToggle")
@Composable
private fun ViewModeTogglePreview_Timeline() {
  TwineTheme {
    ViewModeToggle(
      viewModeToggle = Timeline
    ) {
      // Handle view mode change
    }
  }
}

@Preview(group = "ViewModeToggle")
@Composable
private fun ViewModeTogglePreview_Story() {
  TwineTheme {
    ViewModeToggle(
      viewModeToggle = Story
    ) {
      // Handle view mode change
    }
  }
}

/**
 * A rounded rectangle shape that is clipped to half the size of [ViewModeToggle] button.
 *
 * @param offset: horizontal offset used for moving shape on x-axis
 */
private class SelectorShape(
  private val offset: Offset
) : Shape {

  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val cornerSizePx = with(density) { ViewModeToggleDefaults.selectorCornerRadius.toPx() }
    val buttonSpacingInPx = with(density) { ViewModeToggleDefaults.buttonRowSpacing.toPx() }

    val shapeSizeInPx = (size.width - buttonSpacingInPx) / 2
    val leftOffset = offset.x.coerceAtMost(maximumValue = shapeSizeInPx + buttonSpacingInPx)
    val rightOffset = (leftOffset + shapeSizeInPx).coerceAtMost(size.width)

    return Outline.Rounded(
      RoundRect(
        rect = size.toRect().copy(
          left = leftOffset,
          right = rightOffset
        ),
        topLeft = CornerRadius(cornerSizePx),
        topRight = CornerRadius(cornerSizePx),
        bottomRight = CornerRadius(cornerSizePx),
        bottomLeft = CornerRadius(cornerSizePx)
      )
    )
  }
}
