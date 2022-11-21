package io.mhssn.samplecolorpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.mhssn.colorpicker.*
import io.mhssn.colorpicker.ext.*
import io.mhssn.samplecolorpicker.ui.theme.ColorPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorPickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
    Column(
        modifier = Modifier.padding(32.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var color by remember {
            mutableStateOf(Color.Red)
        }
        var colorPickerType by remember {
            mutableStateOf<ColorPickerType>(ColorPickerType.Classic())
        }
        var showDialog by remember {
            mutableStateOf(false)
        }
        ColorPickerDialog(
            show = showDialog,
            type = colorPickerType,
            properties = DialogProperties(),
            onDismissRequest = {
                showDialog = false
            },
            onPickedColor = {
                showDialog = false
                color = it
            },
        )
        ColorPicker(type = colorPickerType) {
            color = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            val (alpha, red, green, blue) = color.argb()
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Hex")
                Text(text = "#${color.toHex()}")
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Alpha")
                Text(text = alpha.toString())
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Red")
                Text(text = red.toString())
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Green")
                Text(text = green.toString())
            }
            Column(horizontalAlignment = CenterHorizontally) {
                Text(text = "Blue")
                Text(text = blue.toString())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(80.dp, 50.dp)
                .clip(RoundedCornerShape(50))
                .border(0.3.dp, Color.LightGray, RoundedCornerShape(50))
                .transparentBackground(verticalBoxesAmount = 8)
                .background(color)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Color Picker Type", color = Color.Black, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(2),
            content = {
                item {
                    OutlinedButton(onClick = {
                        colorPickerType = ColorPickerType.Classic()
                    }, shape = RoundedCornerShape(50)) {
                        Text(text = "Classic")
                    }
                }
                item {
                    OutlinedButton(onClick = {
                        colorPickerType = ColorPickerType.Circle()
                    }, shape = RoundedCornerShape(50)) {
                        Text(text = "Circle")
                    }
                }
                item {
                    OutlinedButton(onClick = {
                        colorPickerType = ColorPickerType.Ring()
                    }, shape = RoundedCornerShape(50)) {
                        Text(text = "Ring")
                    }
                }
                item {
                    OutlinedButton(onClick = {
                        colorPickerType = ColorPickerType.SimpleRing()
                    }, shape = RoundedCornerShape(50)) {
                        Text(text = "Simple Ring")
                    }
                }
            })
        OutlinedButton(onClick = {
            showDialog = true
        }, shape = RoundedCornerShape(50)) {
            Text(text = "Open dialog")
        }
    }
}