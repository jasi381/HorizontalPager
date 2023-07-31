package com.example.horizontalpager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horizontalpager.states.CounterViewAction
import com.example.horizontalpager.states.CounterViewState
import com.example.horizontalpager.ui.theme.HorizontalPagerTheme
import com.example.horizontalpager.viewModel.CounterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HorizontalPagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = CounterViewModel()
                    UI(viewModel)
                }
            }
        }
    }
}

@Composable
fun UI(viewModel: CounterViewModel) {
    val state by viewModel.state.observeAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = when (state) {
                is CounterViewState.CounterValue -> (state as CounterViewState.CounterValue).count.toString()
                else -> "0"
            },
            color = Color.White,
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp
        )

        Row {
            Button(
                onClick = { viewModel.processIntent(CounterViewAction.Decrement) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Decrement")
            }
            Button(
                onClick = { viewModel.processIntent(CounterViewAction.Increment) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Increment")
            }
            Button(
                onClick = { viewModel.processIntent(CounterViewAction.Reset) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Reset")
            }
        }

    }

}




