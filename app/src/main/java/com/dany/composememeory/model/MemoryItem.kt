package com.dany.composememeory.model

import androidx.compose.ui.graphics.Color
import com.dany.composememeory.ui.theme.CorrectTile
import com.dany.composememeory.ui.theme.HintTile
import com.dany.composememeory.ui.theme.NormalTile
import com.dany.composememeory.ui.theme.WrongTile

data class MemoryItem(
  val x: Int,
  val y: Int,
  val state: TileState = TileState.NORMAL
)

enum class TileState(val color: Color) {
  NORMAL(NormalTile),
  HINT(HintTile),
  CORRECT(CorrectTile),
  WRONG(WrongTile),
  ;
}
