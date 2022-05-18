package com.janayalsalem.tipcalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.janayalsalem.tipcalculatorapp.ui.theme.TipCalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {

            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    TipCalculatorAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme{

    }
}