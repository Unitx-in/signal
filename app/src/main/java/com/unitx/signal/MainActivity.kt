package com.unitx.signal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unitx.signal.ui.theme.SignalTheme
import com.unitx.signal_core.common.type.IconPosition
import com.unitx.signal_core.common.type.SnackPosition
import com.unitx.signal_core.common.type.SnackType
import com.unitx.signal_core.common.type.ToastPosition
import com.unitx.signal_core.common.type.ToastType
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
                        ToastScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun SnackScreen() {
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
            Signal.snack("Second message") {
                type = SnackType.Warning
                position = SnackPosition.Bottom
                action("New"){ Log.i("Snackbar", "New Action Pressed")}
            }
            Signal.snack("Third message") { type = SnackType.Success }
        }) { Text("Test Queue") }
    }
}

@Composable
fun ToastScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.toast("Item saved successfully") { type = ToastType.Success }
        }) { Text("Success Toast") }

        Button(onClick = {
            Signal.toast("Something went wrong") { type = ToastType.Error }
        }) { Text("Error Toast") }

        Button(onClick = {
            Signal.toast("Low storage warning") {
                type = ToastType.Warning
                position = ToastPosition.Top
            }
        }) { Text("Warning Top Toast") }

        Button(onClick = {
            Signal.toast("Update available") {
                type = ToastType.Info
                position = ToastPosition.Center
            }
        }) { Text("Info Center Toast") }

        Button(onClick = {
            Signal.toast("File deleted") {
                type = ToastType.Error
                position = ToastPosition.Top
                iconRes = com.unitx.signal_core.R.drawable.ic_snack_close
                iconPosition = IconPosition.Start
            }
        }) { Text("Toast with Icon") }

        Button(onClick = {
            Signal.toast("First")
            Signal.toast("Second") { type = ToastType.Success }
            Signal.toast("Third") { type = ToastType.Error }
        }) { Text("Rapid Toasts") }
    }
}