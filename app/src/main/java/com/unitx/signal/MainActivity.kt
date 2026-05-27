package com.unitx.signal

import android.R.attr.type
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unitx.signal.ui.theme.SignalTheme
import com.unitx.signal_core.common.SnackType
import com.unitx.signal_core.launcher.Signal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TestScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.snack("Item saved successfully") { type = SnackType.Success }
        }) { Text("Success Snack") }

        Button(onClick = {
            Signal.snack("File uploaded") {
                type = SnackType.Error
                showCancelAction = false
                action("Cancel") { Log.i("Snackbar", "View Action Pressed") }
            }
        }) { Text("Error Snack") }

        Button(onClick = {
            Signal.snack("First message") { type = SnackType.Info }
            Signal.snack("Second message") { type = SnackType.Warning }
            Signal.snack("Third message") { type = SnackType.Success }
        }) { Text("Test Queue") }
    }
}