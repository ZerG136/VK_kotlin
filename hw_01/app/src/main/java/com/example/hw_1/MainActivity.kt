package com.example.hw_1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private lateinit var squaresText: MutableList<String>
    private var squaresNum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            squaresNum = savedInstanceState?.getInt("squaresNum") ?: 0
            squaresText = MutableList(squaresNum) { "$it" }

            squaresText = drawSquares(squaresText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("squaresNum", squaresText.size)
    }
}

@Composable
fun drawSquares(textData: List<String>): MutableList<String> {
    val squaresText = remember { mutableStateListOf(*textData.toTypedArray()) }

    val columnsNum = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> { 3 }
        Configuration.ORIENTATION_LANDSCAPE -> { 4 }
        else -> { 3 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f),
            columns = GridCells.Fixed(columnsNum),
        ) {
            itemsIndexed(squaresText) { index, item ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(5.dp)
                        .background(if (index % 2 != 0) Color.Blue else Color.Red),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = item, fontSize = 50.sp, color = Color.White)
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { squaresText.add("${squaresText.size}") }
            ) {
                Text(text = "Add square", fontSize = 20.sp)
            }
        }
    }

    return squaresText
}