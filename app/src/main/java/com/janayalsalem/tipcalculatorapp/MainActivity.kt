package com.janayalsalem.tipcalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.janayalsalem.tipcalculatorapp.components.InputField
import com.janayalsalem.tipcalculatorapp.ui.theme.TipCalculatorAppTheme

//@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                TopHeader()
//                MainContent() // need a @ExperimentalComposeUiApi
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


@Composable
fun TopHeader(totalPerPers: Double = 0.0) {

    val totalFormat = "%.2f".format(totalPerPers)

    Surface(color = Color(0xFFe9d7f7)
        ,modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
    ) {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text(text = "Total Per Person", style = MaterialTheme.typography.subtitle1)
            Text(text = "\$$totalFormat", style = MaterialTheme.typography.h4)

        } // End Column

    } // End Surface

}

@ExperimentalComposeUiApi
@Composable
fun MainContent() {

    // What did number user enter.
    val totalNumber = remember { mutableStateOf("") }

    // LocalTextInputService.
    val keyboardController = LocalSoftwareKeyboardController.current // need a @ExperimentalComposeUiApi

    // To check totalNumber isNotEmpty
    val valid = remember(totalNumber.value) { totalNumber.value.trim().isNotEmpty() }

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {

        Column(modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            InputField(
                valueState = totalNumber, labelId = "Enter Number",
                enabled = true,
                onAction = KeyboardActions {
                    //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                    if (!valid) return@KeyboardActions
//                    onValChange(totalNumber.value.trim())
                    //totalBill.value = ""
                    keyboardController?.hide() //(to use this we need to use @ExperimentalComposeUiApi
                },
            )

}
    }
}


@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme{
//        TopHeader()
        MainContent()
    }
}