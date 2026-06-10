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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unitx.signal.ui.theme.SignalTheme
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType
import com.unitx.signal_core.main.Signal
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
                        SignalTestScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun DialogScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.dialog {
                title = "Successful purchase!"
                message = "Your order has been placed and will arrive in 3-5 business days."
                type = DialogType.Positive
                dismissOnPositive = false
                positive("Got it") {
                    Signal.toast("Working") {
                        type = ToastType.Success
                        position = ToastPosition.Bottom
                    }
                }
            }
        }) { Text("Success Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Delete file?"
                message = "This action cannot be undone. The file will be permanently removed."
                type = DialogType.Error
                positive("Delete") { Log.i("Dialog", "Deleted") }
                negative("Cancel")
                neutral("Know more") { Log.i("Dialog", "Neutral") }
            }
        }) { Text("Error Dialog with Actions") }

        Button(onClick = {
            Signal.dialog {
                title = "Low storage"
                message = "You are running low on storage. Consider clearing some space."
                type = DialogType.Action
                cancelable = true
                dismissOnNegative = false
                dismissOnPositive = false
                positive("Clear now") { Log.i("Dialog", "Clear pressed") }
                negative("Dismiss") {
                    Signal.dismissDialog()
                }
            }
        }) { Text("Warning cancelable") }

        Button(onClick = {
            Signal.dialog {
                title = "Update available"
                message = "A new version is available. Update now to get the latest features."
                type = DialogType.Default
                positive("Update") { Log.i("Dialog", "Update pressed") }
                negative("Later")
            }
        }) { Text("Info Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Session expiring"
                message = "Your session will expire in 2 minutes."
                type = DialogType.Action
                autoDismiss = true
                autoDismissDuration = 3000L
                positive("Stay logged in") { Log.i("Dialog", "Session extended") }
            }
        }) { Text("Auto Dismiss Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "First dialog"
                message = "This is the first in queue."
                type = DialogType.Default
                positive("Next") { Log.i("Dialog", "First confirmed") }
            }
            Signal.dialog {
                title = "Second dialog"
                message = "This appears after the first is dismissed."
                type = DialogType.Positive
                positive("Done") { Log.i("Dialog", "Second confirmed") }
            }
        }) { Text("Test Queue") }
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
            Signal.toast("Something went wrong") {
                type = ToastType.Error
            }
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

@Composable
fun SignalTestScreen() {
    var selected by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Toast", "Snack", "Dialog").forEachIndexed { index, label ->
                Button(
                    onClick = { selected = index },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selected == index)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { Text(label) }
            }
        }

        when (selected) {
            0 -> ToastScreen()
            1 -> SnackScreen()
            2 -> DialogScreen()
        }
    }
}