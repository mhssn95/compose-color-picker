package io.mhssn.samplecolorpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mhssn.colorpicker.*
import io.mhssn.samplecolorpicker.ui.theme.ColorPickerTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorPickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Greeting()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Greeting() {
    var color by remember {
        mutableStateOf(Color.Red)
    }
    Column(horizontalAlignment = CenterHorizontally) {
        CircleColorPicker {
            color = it
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Red=${color.red()}   Green=${color.green()}   Blue=${color.blue()}")
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.size(80.dp, 50.dp).clip(RoundedCornerShape(90)).background(color))
    }
}