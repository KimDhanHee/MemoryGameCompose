package com.dany.composememeory.ui.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dany.composememeory.model.MemoryItem
import com.dany.composememeory.viewmodel.GameState

@OptIn(ExperimentalUnitApi::class)
@Composable
fun GameStateGuide(
  state: GameState,
  modifier: Modifier = Modifier,
) {
  val titleColor by animateColorAsState(
    targetValue = state.color, 
    animationSpec = TweenSpec(300)
  )
  Text(
    modifier = modifier
      .padding(top = 60.dp)
      .fillMaxWidth(),
    text = stringResource(id = state.titleSrc),
    color = titleColor,
    textAlign = TextAlign.Center,
    fontSize = TextUnit(24f, TextUnitType.Sp)
  )
}

@Preview
@Composable
fun GameStateGuidePreview() {
  GameStateGuide(state = GameState.IN_PROGRESS)
}

@Composable
fun MemoryTileArray(
  tiles: Array<Array<MemoryItem>>,
  onTileClick: (MemoryItem) -> Unit,
) {
  Column {
    tiles.forEach { rows ->
      Row {
        rows.forEach { tile ->
          MemoryTile(
            tile = tile,
            onTileClick = {
              onTileClick(tile)
            }
          )
        }
      }
    }
  }
}

@Composable
private fun MemoryTile(
  tile: MemoryItem,
  onTileClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val tileColor by animateColorAsState(
    targetValue = tile.state.color,
    animationSpec = TweenSpec(durationMillis = 300)
  )
  Button(
    modifier = modifier
      .padding(3.dp)
      .size(64.dp),
    shape = RoundedCornerShape(4.dp),
    onClick = onTileClick,
    colors = ButtonDefaults.buttonColors(
      backgroundColor = tileColor
    )
  ) {}
}

@Preview
@Composable
private fun MemoryTilePreview() {
  MemoryTile(tile = MemoryItem(0, 0), onTileClick = { })
}