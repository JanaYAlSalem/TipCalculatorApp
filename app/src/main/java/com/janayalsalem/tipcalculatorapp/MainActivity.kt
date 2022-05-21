package com.janayalsalem.tipcalculatorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
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
import androidx.compose.runtime.saveable.rememberSaveable



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

    BillForm(splitByState = splitBy, tipAmountState = totalTipAmt, totalPerPersonState = totalPerPerson) {

    }
}


@ExperimentalComposeUiApi
@Composable
fun BillForm(splitByState: MutableState<Int>,
             tipAmountState: MutableState<Double>,
             totalPerPersonState: MutableState<Double>,
             modifier: Modifier = Modifier,
             onValChange: (String) -> Unit = {},
) {

    var sliderPosition = remember { mutableStateOf(0f) }
    val tipPercentage = (sliderPosition.value * 100).toInt()

    val totalBill = rememberSaveable { mutableStateOf("") } //or just remember {}
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(totalBill.value) {
        totalBill.value.trim().isNotEmpty()
    }


    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)) {

        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            TopHeader(totalPerPers = totalPerPersonState.value)

            InputField(
                valueState = totalBill, labelId = "Enter Bill",
                enabled = true,
                onAction = KeyboardActions {
                    //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                    if (!valid) return@KeyboardActions
                    onValChange(totalBill.value.trim())
                    keyboardController?.hide() //(to use this we need to use @ExperimentalComposeUiApi
                },
            )

            if (valid) {
                Split(splitByState,totalPerPersonState,totalBill,tipPercentage)
                Tip(splitByState,totalPerPersonState,tipAmountState,sliderPosition,totalBill,tipPercentage)
            }//end isValid


        }
    }}

@Composable
fun Split (splitByState: MutableState<Int>,
            totalPerPersonState: MutableState<Double>,
            totalBill: MutableState<String>,
            tipPercentage: Int,
            range: IntRange = 1..100) {

    Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
        Text(text = "Split",
            modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(120.dp))

        Row(modifier = Modifier.padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.End) {

            RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                // if splitByState > 1 -> minus one on user clicked
                splitByState.value =
                    if (splitByState.value > 1) splitByState.value - 1 else 1
                totalPerPersonState.value =
                    calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                        splitBy = splitByState.value,
                        tipPercent = tipPercentage)
            })

            Text(text = "${splitByState.value}",
                Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(start = 9.dp, end = 9.dp))
            RoundIconButton(imageVector = Icons.Default.Add, onClick = {
               // add one on user clicked
                if (splitByState.value < range.last) {
                    splitByState.value = splitByState.value + 1

                    totalPerPersonState.value =
                        calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                            splitBy = splitByState.value,
                            tipPercent = tipPercentage)
                }
            })

        }
    }

}


@Composable
fun Tip (splitByState: MutableState<Int>,
         totalPerPersonState: MutableState<Double>,
         tipAmountState: MutableState<Double>,
         sliderPosition: MutableState<Float>,
         totalBill: MutableState<String>, tipPercentage: Int,) {

    Row(modifier = Modifier
        .padding(horizontal = 3.dp)
        .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.End) {
        // Tip section
        Text(text = "Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(200.dp))
        Text(text = "$${tipAmountState.value}", modifier = Modifier.align(alignment = Alignment.CenterVertically))

    }
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "$tipPercentage %")
        Spacer(modifier = Modifier.height(14.dp))

        // Slider( value: Float,onValueChange: (Float) → Unit, valueRange: ClosedFloatingPointRange<Float>, steps: Int, onValueChangeFinished: (() → Unit)?,colors: SliderColors ): Unit
        Slider(value = sliderPosition.value,
            onValueChange = { newVal ->
                sliderPosition.value = newVal
                tipAmountState.value =
                    calculateTotalTip(totalBill = totalBill.value.toDouble(),
                        tipPercent = tipPercentage)

                totalPerPersonState.value =
                    calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                        splitBy = splitByState.value,
                        tipPercent = tipPercentage)
                Log.d("Slider",
                    "Total Bill-->: ${"%.2f".format(totalPerPersonState.value)}")

            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp), steps = 10, onValueChangeFinished = {})

    }

}


fun calculateTotalTip(totalBill: Double, tipPercent: Int): Double {

    return if (totalBill > 1 && totalBill.toString().isNotEmpty()) (totalBill * tipPercent) / 100 else 0.0
}
fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercent: Int): Double {
    val bill = calculateTotalTip(totalBill, tipPercent = tipPercent) + totalBill
    return (bill/splitBy)
}