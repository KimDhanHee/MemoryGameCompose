package com.dany.composememeory.viewmodel

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dany.composememeory.R
import com.dany.composememeory.model.MemoryItem
import com.dany.composememeory.model.TileState
import com.dany.composememeory.ui.theme.GameStateClear
import com.dany.composememeory.ui.theme.GameStateCorrect
import com.dany.composememeory.ui.theme.GameStateHint
import com.dany.composememeory.ui.theme.GameStateInProgress
import com.dany.composememeory.ui.theme.GameStateWrong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoryViewModel : ViewModel() {
  private val rows: Int = 5
  private val cols: Int = 5

  private val mainScope = CoroutineScope(Dispatchers.Main)

  var gameState by mutableStateOf(GameState.IDLE)

  private var wrongCount = 0

  var tiles by mutableStateOf(Array(cols) { y ->
    Array(rows) { x ->
      MemoryItem(x, y)
    }
  })

  private val answerTilesPos = (0 until rows * cols).shuffled().take(5).map {
    val x = it % cols
    val y = it / cols
    x to y
  }.toMutableList()

  fun start() {
    mainScope.launch {
      displayHint()
      delay(2000)
      resetTiles()
    }
  }

  private fun displayHint() {
    gameState = GameState.HINT
    tiles = tiles.clone().apply {
      answerTilesPos.forEach { (x, y) ->
        this[y][x] = this[y][x].copy(state = TileState.HINT)
      }
    }
  }

  private fun resetTiles() {
    gameState = GameState.IN_PROGRESS
    tiles = tiles.clone().apply {
      answerTilesPos.forEach { (x, y) ->
        this[y][x] = this[y][x].copy(state = TileState.NORMAL)
      }
    }
  }

  fun onTileClick(tile: MemoryItem) {
    if (tile.state == TileState.CORRECT || gameState != GameState.IN_PROGRESS) return

    val isAnswer = answerTilesPos.contains(tile.x to tile.y)

    when (isAnswer) {
      true -> onCorrectTileClick(tile)
      false -> onWrongTileClick(tile)
    }
  }

  private fun onCorrectTileClick(tile: MemoryItem) = mainScope.launch {
    gameState = GameState.CORRECT
    tiles = tiles.clone().apply {
      this[tile.y][tile.x] = tile.copy(state = TileState.CORRECT)
      answerTilesPos.remove(tile.x to tile.y)
    }

    delay(500)

    gameState = when {
      answerTilesPos.isEmpty() -> GameState.CLEAR
      else -> GameState.IN_PROGRESS
    }
  }

  private fun onWrongTileClick(tile: MemoryItem) = mainScope.launch {
    wrongCount++

    gameState = GameState.WRONG
    tiles = tiles.clone().apply {
      this[tile.y][tile.x] = tile.copy(state = TileState.WRONG)
    }
    delay(1000)

    if (wrongCount >= DISPLAY_HINT_WRONG_COUNT) {
      displayHint()
      delay(2000)
      resetTiles()
      wrongCount = 0
    }

    gameState = GameState.IN_PROGRESS
    tiles = tiles.clone().apply {
      this[tile.y][tile.x] = tile.copy(state = TileState.NORMAL)
    }
  }

  companion object {
    private const val DISPLAY_HINT_WRONG_COUNT = 3
  }
}

enum class GameState(@StringRes val titleSrc: Int, val color: Color) {
  IDLE(R.string.game_state_idle, Color.Transparent),
  IN_PROGRESS(R.string.game_state_in_progress, GameStateInProgress),
  WRONG(R.string.game_state_wrong, GameStateWrong),
  CORRECT(R.string.game_state_correct, GameStateCorrect),
  HINT(R.string.game_state_hint, GameStateHint),
  CLEAR(R.string.game_state_clear, GameStateClear),
  ;
}