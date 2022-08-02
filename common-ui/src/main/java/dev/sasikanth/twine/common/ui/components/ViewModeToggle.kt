package dev.sasikanth.twine.common.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Story
import dev.sasikanth.twine.common.ui.components.ViewModeToggle.Timeline
import dev.sasikanth.twine.common.ui.theme.ElevationTokens
import dev.sasikanth.twine.common.ui.theme.TwineTheme
import dev.sasikanth.twine.common.ui.theme.surfaceColorAtElevation
import kotlin.math.roundToInt

private val SELECTOR_CORNER_RADIUS = 16.dp
private val CONTAINER_CORNER_RADIUS = 20.dp
private val CONTAINER_PADDING = 4.dp
private val BUTTON_ROW_SPACING = 4.dp
private val ICON_PADDING = 16.dp

enum class ViewModeToggle {
  Timeline,
  Story
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewModeToggle(
  modifier: Modifier = Modifier,
  viewModeToggle: ViewModeToggle,
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
      swipeableState.animateTo(viewModeToggle)
    }
  }

  DisposableEffect(swipeableState.currentValue) {
    if (viewModeToggle != swipeableState.currentValue) {
      onViewModeChanged(swipeableState.currentValue)
      forceAnimationCheck.value = !forceAnimationCheck.value
    }
    onDispose { }
  }

  val buttonSpacingInPx = with(LocalDensity.current) { BUTTON_ROW_SPACING.roundToPx() }
  val selectedButtonWidth = if (size.width == 0f) {
    // temp value until we get the actual size
    1f
  } else {
    (size.width + buttonSpacingInPx) / 2
  }

  val anchors = mapOf(
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
        shape = RoundedCornerShape(CONTAINER_CORNER_RADIUS)
      )
      .padding(CONTAINER_PADDING)
  ) {
    IconButtonRow(onViewModeChanged = onViewModeChanged)

    SelectorLayout(
      modifier = Modifier
        .onSizeChanged { size = it.toSize() }
        .testTag("ViewMode:SelectorLayout"),
      swipeableState = swipeableState,
      anchors = anchors,
    )
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SelectorLayout(
  modifier: Modifier = Modifier,
  swipeableState: SwipeableState<ViewModeToggle>,
  anchors: Map<Float, ViewModeToggle>,
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
        ),
      ),
    horizontalArrangement = Arrangement.spacedBy(BUTTON_ROW_SPACING)
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
fun IconButtonRow(
  modifier: Modifier = Modifier,
  onViewModeChanged: (ViewModeToggle) -> Unit
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(BUTTON_ROW_SPACING),
  ) {
    IconButton(
      modifier = Modifier
        .testTag("ViewMode:Timeline"),
      resource = R.drawable.ic_view_mode_timeline,
      iconTint = TwineTheme.colorScheme.onPrimaryContainer,
      contentDescription = stringResource(id = R.string.cd_view_mode_timeline)
    ) {
      onViewModeChanged(Timeline)
    }

    IconButton(
      modifier = Modifier
        .testTag("ViewMode:Story"),
      resource = R.drawable.ic_view_mode_story,
      iconTint = TwineTheme.colorScheme.onPrimaryContainer,
      contentDescription = stringResource(id = R.string.cd_view_mode_story)
    ) {
      onViewModeChanged(Story)
    }
  }
}

@Composable
private fun IconButton(
  modifier: Modifier = Modifier,
  @DrawableRes resource: Int,
  iconTint: Color,
  contentDescription: String? = null,
  onClick: () -> Unit
) {
  Icon(
    modifier = modifier
      .clip(TwineTheme.shapes.large)
      .clickable(
        onClick = onClick,
        role = Role.Button
      )
      .padding(ICON_PADDING),
    painter = painterResource(id = resource),
    contentDescription = contentDescription,
    tint = iconTint
  )
}

@Composable
fun TimelineIcon(
  modifier: Modifier = Modifier,
  iconTint: Color
) {
  Icon(
    modifier = modifier.padding(ICON_PADDING),
    painter = painterResource(id = R.drawable.ic_view_mode_timeline),
    contentDescription = stringResource(id = R.string.cd_view_mode_timeline),
    tint = iconTint
  )
}

@Composable
fun StoryIcon(
  modifier: Modifier = Modifier,
  iconTint: Color
) {
  Icon(
    modifier = modifier.padding(ICON_PADDING),
    painter = painterResource(id = R.drawable.ic_view_mode_story),
    contentDescription = stringResource(id = R.string.cd_view_mode_story),
    tint = iconTint
  )
}

@Preview(group = "ViewModeToggle")
@Composable
fun ViewModeTogglePreview_Timeline() {
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
fun ViewModeTogglePreview_Story() {
  TwineTheme {
    ViewModeToggle(
      viewModeToggle = Story
    ) {
      // Handle view mode change
    }
  }
}

private class SelectorShape(
  private val offset: Offset,
) : Shape {

  override fun createOutline(
    size: Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val cornerSizePx = with(density) { SELECTOR_CORNER_RADIUS.toPx() }
    val buttonSpacingInPx = with(density) { BUTTON_ROW_SPACING.toPx() }

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