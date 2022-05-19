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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.janayalsalem.tipcalculatorapp.components.InputField
import com.janayalsalem.tipcalculatorapp.components.RoundIconButton
import com.janayalsalem.tipcalculatorapp.ui.theme.TipCalculatorAppTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainContent() // need to use @ExperimentalComposeUiApi
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

    Surface(
        color = Color(0xFFe9d7f7),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Total Per Person", style = MaterialTheme.typography.subtitle1)
            Text(text = "\$$totalFormat", style = MaterialTheme.typography.h4)

        } // End Column

    } // End Surface

}

@ExperimentalComposeUiApi
@Composable
fun MainContent() {

    val splitBy = remember { mutableStateOf(1) }
    val totalTipAmt = remember { mutableStateOf(0.0) }
    val totalPerPerson = remember { mutableStateOf(0.0) }


    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {

        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TopHeader()
            InputPriceField(splitByState = splitBy)

        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun InputPriceField(
    modifier: Modifier = Modifier,
    splitByState: MutableState<Int>,
    onValChange: (String) -> Unit = {},
) {

    // What did number user enter.
    val totalNumber = remember { mutableStateOf("") }

    // LocalTextInputService.
    val keyboardController =
        LocalSoftwareKeyboardController.current // need to use @ExperimentalComposeUiApi

    // To check totalNumber isNotEmpty
    val valid = remember(totalNumber.value) { totalNumber.value.trim().isNotEmpty() }

    InputField(valueState = totalNumber, labelId = "Enter Number", enabled = true, onAction = KeyboardActions {
            //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
            if (!valid) return@KeyboardActions
            onValChange(totalNumber.value.trim()) // get price
            //totalBill.value = ""
            keyboardController?.hide() //need to use @ExperimentalComposeUiApi
        },
    )
    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }

    // show a Split section if enter number
    if (valid) {
        Split(splitByState, totalNumber, totalPerPerson)
    }
}

@Composable
fun Split(
          splitByState: MutableState<Int>,
          totalNumber: MutableState<String>,
          totalPerPersonState: MutableState<Double>,
          range: IntRange = 1..100,) {

    var sliderPosition by remember { mutableStateOf(0f) }
    val tipPercentage = (sliderPosition * 100).toInt()


        Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
            Text(
                text = "Split",
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(120.dp))

            Row(
                modifier = Modifier.padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.End
            ) {

                RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                    splitByState.value =
                        if (splitByState.value > 1) splitByState.value - 1 else 1
                    totalPerPersonState.value = calculateTotalPerPerson(totalBill = totalNumber.value.toDouble(), splitBy = splitByState.value, tipPercent = tipPercentage)

                })
                Text(text = "${splitByState.value}",
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(start = 9.dp, end = 9.dp))
                RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                    if (splitByState.value < range.last) {
                        splitByState.value = splitByState.value + 1

                        totalPerPersonState.value =
                            calculateTotalPerPerson(totalBill = totalNumber.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercent = tipPercentage)
                    }
                })
            }
        }

}


fun calculateTotalTip(totalBill: Double, tipPercent: Int): Double {

    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) (totalBill * tipPercent) / 100 else 0.0
}
fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercent: Int): Double {
    val bill = calculateTotalTip(totalBill, tipPercent = tipPercent) + totalBill
    return (bill/splitBy)
}


@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        MainContent()
    }
}