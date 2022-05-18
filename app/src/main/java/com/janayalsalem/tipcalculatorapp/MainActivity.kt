package com.janayalsalem.tipcalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.janayalsalem.tipcalculatorapp.ui.theme.TipCalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                TopHeader()
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



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme{
        TopHeader()
    }
}