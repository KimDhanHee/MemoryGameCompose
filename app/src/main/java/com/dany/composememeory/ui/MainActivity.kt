package com.dany.composememeory.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dany.composememeory.model.MemoryItem
import com.dany.composememeory.ui.compose.GameStateGuide
import com.dany.composememeory.ui.compose.MemoryArray
import com.dany.composememeory.ui.theme.ComposeMemeoryTheme
import com.dany.composememeory.viewmodel.MemoryViewModel

class MainActivity : ComponentActivity() {
  private val memoryViewModel by viewModels<MemoryViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MemoryGame(viewModel = memoryViewModel)
    }

    memoryViewModel.start()
  }
}

@Composable
fun MemoryGame(viewModel: MemoryViewModel) {
  ComposeMemeoryTheme {
    Column {
      GameStateGuide(state = viewModel.gameState)

      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        MemoryArray(tiles = viewModel.tiles, onTileClick = {
          viewModel.onTileClick(it)
        })
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  val tiles = Array(5) { y ->
    Array(5) { x ->
      MemoryItem(x, y)
    }
  }
  ComposeMemeoryTheme {
    MemoryArray(tiles = tiles, onTileClick = {})
  }
}